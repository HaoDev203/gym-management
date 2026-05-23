package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.response.OrderResponse;
import com.gym.entity.*;
import com.gym.enums.OrderStatus;
import com.gym.mapper.*;
import com.gym.service.OrderService;
import com.gym.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;

    private final MemberMapper memberMapper;

    private final CourseMapper courseMapper;

    private final CoachMapper coachMapper;

    private final MemberService memberService;

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = orderMapper.selectById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        return toOrderResponse(order);
    }

    @Override
    public List<OrderResponse> listMemberOrders(Long memberId) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getMemberId, memberId);
        wrapper.eq(Order::getIsDeleted, 0);
        wrapper.orderByDesc(Order::getCreatedAt);
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::toOrderResponse).collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> listAllOrders() {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Order::getCreatedAt);
        List<Order> orders = orderMapper.selectList(wrapper);
        return orders.stream().map(this::toOrderResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderResponse confirmPayment(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!order.canConfirmPayment()) {
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
        order.confirmPayment();
        order.setPaidAmount(order.getAmount());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        Course course = courseMapper.selectById(order.getCourseId());
        if (course != null) {
            course.setBookedCount(course.getBookedCount() + 1);
            courseMapper.updateById(course);
        }

        return toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse checkIn(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!order.canCheckIn()) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CHECK_IN);
        }

        Course course = courseMapper.selectById(order.getCourseId());
        if (course == null || course.getStartTime() == null) {
            throw new BusinessException(ErrorCode.COURSE_NOT_FOUND);
        }
        LocalDateTime now = LocalDateTime.now();
        if (course.getStartTime().isAfter(now.plusMinutes(30))
                || course.getStartTime().plusHours(2).isBefore(now)) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CHECK_IN, "不在签到时间范围内");
        }

        order.checkIn();
        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        return toOrderResponse(order);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setStatus(OrderStatus.CANCELLED.getCode());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void markNoShow(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setStatus(OrderStatus.NO_SHOW.getCode());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);

        memberService.incrementNoShowCount(order.getMemberId());
    }

    @Override
    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        order.setIsDeleted(1);
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
    }

    private OrderResponse toOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
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

        Member member = memberMapper.selectById(order.getMemberId());
        if (member != null) {
            response.setMemberName(member.getName());
        }

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
