package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.dto.response.*;
import com.gym.entity.*;
import com.gym.enums.OrderStatus;
import com.gym.mapper.*;
import com.gym.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final MemberMapper memberMapper;

    private final CourseMapper courseMapper;

    private final OrderMapper orderMapper;

    @Override
    public StatisticsResponse getOverview() {
        StatisticsResponse response = new StatisticsResponse();

        response.setTotalMembers(memberMapper.selectCount(null));
        response.setTotalCourses(courseMapper.selectCount(null));

        LambdaQueryWrapper<Course> activeWrapper = new LambdaQueryWrapper<>();
        activeWrapper.eq(Course::getStatus, 1);
        response.setActiveCourses(courseMapper.selectCount(activeWrapper));

        response.setActiveMembers(response.getTotalMembers());

        LambdaQueryWrapper<Order> allWrapper = new LambdaQueryWrapper<>();
        allWrapper.in(Order::getStatus,
                OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        response.setTotalRevenue(calcRevenue(allWrapper));

        LambdaQueryWrapper<Order> todayWrapper = new LambdaQueryWrapper<>();
        todayWrapper.ge(Order::getCreatedAt, LocalDate.now().atStartOfDay());
        todayWrapper.in(Order::getStatus,
                OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        response.setRevenueToday(calcRevenue(todayWrapper));

        LambdaQueryWrapper<Order> weekWrapper = new LambdaQueryWrapper<>();
        weekWrapper.ge(Order::getCreatedAt, LocalDate.now().minusDays(7).atStartOfDay());
        weekWrapper.in(Order::getStatus,
                OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        response.setRevenueWeek(calcRevenue(weekWrapper));

        LambdaQueryWrapper<Order> monthWrapper = new LambdaQueryWrapper<>();
        monthWrapper.ge(Order::getCreatedAt, LocalDate.now().minusDays(30).atStartOfDay());
        monthWrapper.in(Order::getStatus,
                OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        response.setRevenueMonth(calcRevenue(monthWrapper));

        LambdaQueryWrapper<Order> bookingWrapper = new LambdaQueryWrapper<>();
        bookingWrapper.in(Order::getStatus,
                OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        response.setTotalBookings(orderMapper.selectCount(bookingWrapper));

        response.setBookingTrend(buildTrend(false));
        response.setRevenueTrend(buildTrend(true));
        response.setTopCourses(buildTopCourses());
        response.setDistribution(buildDistribution());

        return response;
    }

    private List<DailyTrend> buildTrend(boolean withAmount) {
        List<DailyTrend> trend = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 6; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            LocalDateTime dayStart = date.atStartOfDay();
            LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

            LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
            wrapper.ge(Order::getCreatedAt, dayStart);
            wrapper.lt(Order::getCreatedAt, dayEnd);
            wrapper.in(Order::getStatus,
                    OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());

            DailyTrend dt = new DailyTrend();
            dt.setDate(date.toString());
            if (withAmount) {
                dt.setAmount(calcRevenue(wrapper));
            } else {
                List<Order> orders = orderMapper.selectList(wrapper);
                dt.setCount((long) orders.size());
            }
            trend.add(dt);
        }
        return trend;
    }

    private List<CourseRank> buildTopCourses() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Order::getStatus,
                OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        List<Order> orders = orderMapper.selectList(wrapper);

        Map<Long, Long> countMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getCourseId, Collectors.counting()));

        Map<Long, BigDecimal> revenueMap = orders.stream()
                .collect(Collectors.groupingBy(Order::getCourseId,
                        Collectors.reducing(BigDecimal.ZERO, o -> o.getPaidAmount() != null ? o.getPaidAmount() : BigDecimal.ZERO, BigDecimal::add)));

        return countMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .map(e -> {
                    Long courseId = e.getKey();
                    Course course = courseMapper.selectById(courseId);
                    CourseRank rank = new CourseRank();
                    rank.setCourseName(course != null ? course.getName() : "课程 ID:" + courseId);
                    rank.setBookingCount(e.getValue());
                    rank.setRevenue(revenueMap.getOrDefault(courseId, BigDecimal.ZERO));
                    return rank;
                })
                .collect(Collectors.toList());
    }

    private Distribution buildDistribution() {
        Distribution dist = new Distribution();

        List<Course> courses = courseMapper.selectList(null);
        dist.setGroupClass(courses.stream().filter(c -> c.getType() != null && c.getType() == 1).count());
        dist.setPrivateClass(courses.stream().filter(c -> c.getType() != null && c.getType() == 2).count());
        dist.setActiveCourse(courses.stream().filter(c -> c.getStatus() != null && c.getStatus() == 1).count());
        dist.setCancelledCourse(courses.stream().filter(c -> c.getStatus() != null && c.getStatus() == 0).count());

        List<Order> orders = orderMapper.selectList(null);
        dist.setCompletedOrder(orders.stream().filter(o -> o.getStatus() != null && o.getStatus() == OrderStatus.COMPLETED.getCode()).count());
        dist.setNoShowOrder(orders.stream().filter(o -> o.getStatus() != null && o.getStatus() == OrderStatus.NO_SHOW.getCode()).count());

        return dist;
    }

    private BigDecimal calcRevenue(LambdaQueryWrapper<Order> wrapper) {
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream()
                .map(o -> o.getPaidAmount() != null ? o.getPaidAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
