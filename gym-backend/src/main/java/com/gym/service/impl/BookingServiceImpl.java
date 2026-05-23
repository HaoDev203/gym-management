package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.BookingRequest;
import com.gym.dto.response.BookingResponse;
import com.gym.entity.*;
import com.gym.enums.OrderStatus;
import com.gym.mapper.CoachMapper;
import com.gym.mapper.CourseMapper;
import com.gym.mapper.MemberMapper;
import com.gym.mapper.OrderMapper;
import com.gym.service.BookingService;
import com.gym.service.WaitlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 预约服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final int MAX_DAILY_BOOKINGS = 3;
    private static final int MAX_ADVANCE_DAYS = 7;

    private final MemberMapper memberMapper;

    private final CourseMapper courseMapper;

    private final OrderMapper orderMapper;

    private final CoachMapper coachMapper;

    private final WaitlistService waitlistService;

    @Override
    @Transactional
    public BookingResponse book(Long memberId, BookingRequest request) {
        Member member = memberMapper.selectById(memberId);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (member.isBanned()) {
            throw new BusinessException(ErrorCode.MEMBER_BANNED);
        }

        Course course = courseMapper.selectById(request.getCourseId());
        if (course == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        if (course.getStatus() != null && course.getStatus() == 0) {
            throw new BusinessException(ErrorCode.COURSE_CANCELLED);
        }
        if (!course.isBookable()) {
            throw new BusinessException(ErrorCode.COURSE_FULL);
        }

        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getMemberId, memberId);
        orderWrapper.eq(Order::getCourseId, request.getCourseId());
        orderWrapper.eq(Order::getStatus, OrderStatus.PAID.getCode());
        if (orderMapper.selectCount(orderWrapper) > 0) {
            throw new BusinessException(ErrorCode.ORDER_DUPLICATE);
        }

        LocalDateTime courseStart = course.getStartTime();
        LocalDateTime courseEnd = course.getEndTime();

        if (courseStart == null || courseEnd == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }

        if (courseStart.isAfter(LocalDateTime.now().plusDays(MAX_ADVANCE_DAYS))) {
            throw new BusinessException(ErrorCode.ORDER_TOO_EARLY);
        }

        // 团课（type=1）需要检查截止时间，私教课（type=0）不过期
        if (course.getType() == 1 && course.isCutoffPassed()) {
            throw new BusinessException(ErrorCode.ORDER_CUTOFF_PASSED);
        }

        checkTimeConflict(memberId, courseStart, courseEnd, course.getId());

        checkDailyLimit(memberId, courseStart.toLocalDate());

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setMemberId(memberId);
        order.setCourseId(course.getId());
        order.setStatus(OrderStatus.PAID.getCode());
        order.setAmount(course.getPrice());
        order.setPaidAmount(course.getPrice());
        order.setCheckIn(0);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        course.incrementBookedCount();
        courseMapper.updateById(course);

        return toBookingResponse(order);
    }

    @Override
    @Transactional
    public void cancel(Long memberId, Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!order.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        if (!order.canCancel()) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        Course course = courseMapper.selectById(order.getCourseId());
        if (course != null && course.getStartTime() != null
                && LocalDateTime.now().plusHours(2).isAfter(course.getStartTime())) {
            throw new BusinessException(ErrorCode.ORDER_CANCEL_TOO_LATE);
        }

        order.cancel();
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        if (course != null && course.getBookedCount() > 0) {
            course.decrementBookedCount();
            courseMapper.updateById(course);
        }

        try {
            waitlistService.promoteFirst(order.getCourseId());
        } catch (Exception ignored) {
        }
    }

    private void checkTimeConflict(Long memberId, LocalDateTime courseStart, LocalDateTime courseEnd, Long excludeCourseId) {
        LambdaQueryWrapper<Order> conflictWrapper = new LambdaQueryWrapper<>();
        conflictWrapper.eq(Order::getMemberId, memberId);
        conflictWrapper.eq(Order::getStatus, OrderStatus.PAID.getCode());
        List<Order> bookedOrders = orderMapper.selectList(conflictWrapper);

        for (Order bookedOrder : bookedOrders) {
            Course bookedCourse = courseMapper.selectById(bookedOrder.getCourseId());
            if (bookedCourse == null || bookedCourse.getId().equals(excludeCourseId)) {
                continue;
            }
            LocalDateTime bookedStart = bookedCourse.getStartTime();
            LocalDateTime bookedEnd = bookedCourse.getEndTime();
            if (bookedStart == null || bookedEnd == null) {
                continue;
            }
            if (courseStart.isBefore(bookedEnd) && courseEnd.isAfter(bookedStart)) {
                throw new BusinessException(ErrorCode.ORDER_TIME_CONFLICT);
            }
        }
    }

    private void checkDailyLimit(Long memberId, LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

        LambdaQueryWrapper<Order> limitWrapper = new LambdaQueryWrapper<>();
        limitWrapper.eq(Order::getMemberId, memberId);
        limitWrapper.in(Order::getStatus, OrderStatus.PAID.getCode(), OrderStatus.COMPLETED.getCode());
        limitWrapper.ge(true, Order::getCreatedAt, dayStart);
        limitWrapper.lt(true, Order::getCreatedAt, dayEnd);

        long count = orderMapper.selectCount(limitWrapper);
        if (count >= MAX_DAILY_BOOKINGS) {
            throw new BusinessException(ErrorCode.ORDER_DAILY_LIMIT);
        }
    }

    private String generateOrderNo() {
        return "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    private BookingResponse toBookingResponse(Order order) {
        BookingResponse response = new BookingResponse();
        response.setId(order.getId());
        response.setOrderNo(order.getOrderNo());
        response.setMemberId(order.getMemberId());
        response.setCourseId(order.getCourseId());
        response.setStatus(order.getStatus());
        response.setAmount(order.getAmount());
        response.setPaidAmount(order.getPaidAmount());
        response.setCheckIn(order.getCheckIn());
        response.setCheckInTime(order.getCheckInTime());
        response.setCreatedAt(order.getCreatedAt());

        Course course = courseMapper.selectById(order.getCourseId());
        if (course != null) {
            response.setCourseName(course.getName());
            response.setStartTime(course.getStartTime());
            response.setEndTime(course.getEndTime());
            Coach coach = coachMapper.selectById(course.getCoachId());
            if (coach != null) {
                response.setCoachName(coach.getName());
            }
        }
        return response;
    }
}
