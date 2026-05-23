package com.gym.service;

import com.gym.dto.request.BookingRequest;
import com.gym.dto.response.BookingResponse;

/**
 * 预约服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface BookingService {

    /**
     * 预约课程。
     *
     * @param memberId 会员 ID
     * @param request  预约请求
     * @return 预约响应
     */
    BookingResponse book(Long memberId, BookingRequest request);

    /**
     * 取消预约。
     *
     * @param memberId 会员 ID
     * @param orderId  订单 ID
     */
    void cancel(Long memberId, Long orderId);
}
