package com.gym.service;

import com.gym.dto.response.OrderResponse;

import java.util.List;

/**
 * 订单管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface OrderService {

    OrderResponse getOrderById(Long id);

    List<OrderResponse> listMemberOrders(Long memberId);

    List<OrderResponse> listAllOrders();

    OrderResponse confirmPayment(Long orderId);

    OrderResponse checkIn(Long orderId);

    OrderResponse adminCheckIn(Long orderId);

    void cancelOrder(Long orderId);

    void markNoShow(Long orderId);

    void deleteOrder(Long orderId);

    /**
     * 标记订单为已支付。
     *
     * @param orderId 订单 ID
     * @return 订单响应
     */
    OrderResponse markAsPaid(Long orderId);

    /**
     * 标记订单为未支付。
     *
     * @param orderId 订单 ID
     * @return 订单响应
     */
    OrderResponse markAsUnpaid(Long orderId);
}
