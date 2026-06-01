package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.response.OrderResponse;
import com.gym.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单管理接口控制器。
 *
 * <p>提供订单查询、订单列表、确认支付、签到、取消、爽约标记、删除订单等接口。</p>
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    public BaseResponse<OrderResponse> getOrder(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return BaseResponse.success(order);
    }

    @GetMapping("/member/{memberId}")
    public BaseResponse<List<OrderResponse>> listMemberOrders(@PathVariable Long memberId) {
        List<OrderResponse> orders = orderService.listMemberOrders(memberId);
        return BaseResponse.success(orders);
    }

    @GetMapping("/list")
    public BaseResponse<List<OrderResponse>> listAllOrders() {
        List<OrderResponse> orders = orderService.listAllOrders();
        return BaseResponse.success(orders);
    }

    @PutMapping("/confirm/{id}")
    public BaseResponse<OrderResponse> confirmPayment(@PathVariable Long id) {
        OrderResponse order = orderService.confirmPayment(id);
        return BaseResponse.success(order);
    }

    @PutMapping("/checkin/{id}")
    public BaseResponse<Void> checkIn(@PathVariable Long id) {
        orderService.checkIn(id);
        return BaseResponse.success(null);
    }

    @PutMapping("/admin-checkin/{id}")
    public BaseResponse<Void> adminCheckIn(@PathVariable Long id) {
        orderService.adminCheckIn(id);
        return BaseResponse.success(null);
    }

    @PutMapping("/cancel/{id}")
    public BaseResponse<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return BaseResponse.success(null);
    }

    @PutMapping("/no-show/{id}")
    public BaseResponse<Void> markNoShow(@PathVariable Long id) {
        orderService.markNoShow(id);
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return BaseResponse.success(null);
    }

    @PutMapping("/mark-paid/{id}")
    public BaseResponse<OrderResponse> markAsPaid(@PathVariable Long id) {
        OrderResponse order = orderService.markAsPaid(id);
        return BaseResponse.success(order);
    }

    @PutMapping("/mark-unpaid/{id}")
    public BaseResponse<OrderResponse> markAsUnpaid(@PathVariable Long id) {
        OrderResponse order = orderService.markAsUnpaid(id);
        return BaseResponse.success(order);
    }
}
