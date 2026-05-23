package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.CourseRequest;
import com.gym.dto.response.CourseResponse;
import com.gym.entity.Coach;
import com.gym.entity.Course;
import com.gym.entity.Order;
import com.gym.enums.OrderStatus;
import com.gym.mapper.CoachMapper;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.OrderMapper;
import com.gym.service.CourseService;
import com.gym.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 课程管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;

    private final CoachMapper coachMapper;

    private final OrderMapper orderMapper;

    private final NotificationService notificationService;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        Course course = new Course();
        course.setName(request.getName());
        course.setType(request.getType());
        course.setCoachId(request.getCoachId());
        course.setStartTime(request.getStartTime());
        course.setEndTime(request.getEndTime());
        course.setCapacity(request.getCapacity());
        course.setBookedCount(0);
        course.setPrice(request.getPrice());
        course.setStatus(1);
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());
        courseMapper.insert(course);
        return toCourseResponse(course);
    }

    @Override
    public List<CourseResponse> listBookableCourses() {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Course::getStartTime);
        List<Course> courses = courseMapper.selectList(wrapper);
        return courses.stream().map(this::toCourseResponse).collect(Collectors.toList());
    }

    @Override
    public List<CourseResponse> listAllCourses() {
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Course::getCreatedAt);
        List<Course> courses = courseMapper.selectList(wrapper);
        return courses.stream().map(this::toCourseResponse).collect(Collectors.toList());
    }

    @Override
    public CourseResponse getCourseById(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        return toCourseResponse(course);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (request.getName() != null) {
            course.setName(request.getName());
        }
        if (request.getType() != null) {
            course.setType(request.getType());
        }
        if (request.getCoachId() != null) {
            course.setCoachId(request.getCoachId());
        }
        if (request.getStartTime() != null) {
            course.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            course.setEndTime(request.getEndTime());
        }
        if (request.getCapacity() != null) {
            course.setCapacity(request.getCapacity());
        }
        if (request.getPrice() != null) {
            course.setPrice(request.getPrice());
        }
        course.setUpdatedAt(LocalDateTime.now());
        courseMapper.updateById(course);
        return toCourseResponse(course);
    }

    @Override
    @Transactional
    public void cancelCourse(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        course.cancel();
        course.setUpdatedAt(LocalDateTime.now());

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getCourseId, id);
        orderWrapper.eq(Order::getStatus, OrderStatus.PAID.getCode());
        List<Order> paidOrders = orderMapper.selectList(orderWrapper);

        int cancelledCount = 0;
        for (Order order : paidOrders) {
            order.setStatus(OrderStatus.CANCELLED.getCode());
            order.setUpdatedAt(LocalDateTime.now());
            orderMapper.updateById(order);
            cancelledCount++;
            notificationService.send(order.getMemberId(), "课程取消通知",
                    "您预约的课程「" + course.getName() + "」已被取消，如有疑问请联系管理员。", 6);
        }

        if (cancelledCount > 0 && course.getBookedCount() != null) {
            course.setBookedCount(Math.max(0, course.getBookedCount() - cancelledCount));
        }
        courseMapper.updateById(course);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        courseMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void restoreCourse(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        course.setStatus(1);
        course.setUpdatedAt(LocalDateTime.now());
        courseMapper.updateById(course);
    }

    private CourseResponse toCourseResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setName(course.getName());
        response.setType(course.getType());
        response.setCoachId(course.getCoachId());
        if (course.getCoachId() != null) {
            Coach coach = coachMapper.selectById(course.getCoachId());
            if (coach != null) {
                response.setCoachName(coach.getName());
            }
        }
        response.setStartTime(course.getStartTime());
        response.setEndTime(course.getEndTime());
        response.setCapacity(course.getCapacity());
        response.setBookedCount(course.getBookedCount());
        response.setPrice(course.getPrice());
        response.setStatus(course.getStatus());
        response.setCreatedAt(course.getCreatedAt());
        response.setIsExpired(course.isExpired());
        return response;
    }
}
