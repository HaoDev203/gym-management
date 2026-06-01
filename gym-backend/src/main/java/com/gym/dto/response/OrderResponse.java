package com.gym.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class OrderResponse {

    private Long id;

    private String orderNo;

    private Long memberId;

    private String memberName;

    private Long courseId;

    private String courseName;

    private String coachName;

    private Integer status;

    private BigDecimal amount;

    private BigDecimal paidAmount;

    private Integer checkIn;

    private LocalDateTime checkInTime;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createdAt;
}
