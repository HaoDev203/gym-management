package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.BookingRequest;
import com.gym.dto.response.BookingResponse;
import com.gym.dto.response.OrderResponse;
import com.gym.service.BookingService;
import com.gym.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 课程预约接口控制器。
 *
 * <p>提供课程预约、取消预约等接口。</p>
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/book")
    public BaseResponse<BookingResponse> bookCourse(@RequestParam Long memberId, @RequestBody @Valid BookingRequest request) {
        BookingResponse response = bookingService.book(memberId, request);
        return BaseResponse.success(response);
    }

    @PostMapping("/cancel")
    public BaseResponse<Void> cancelBooking(@RequestParam Long memberId, @RequestParam Long orderId) {
        bookingService.cancel(memberId, orderId);
        return BaseResponse.success(null);
    }

    @GetMapping("/member/{memberId}")
    public BaseResponse<List<OrderResponse>> listMemberBookings(@PathVariable Long memberId) {
        List<OrderResponse> orders = orderService.listMemberOrders(memberId);
        return BaseResponse.success(orders);
    }
}
