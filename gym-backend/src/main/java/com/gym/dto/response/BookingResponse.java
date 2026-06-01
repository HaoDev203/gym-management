package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 预约响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class BookingResponse {

    private Long id;

    private String orderNo;

    private Long memberId;

    private Long courseId;

    private String courseName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String coachName;

    private Integer status;

    private java.math.BigDecimal amount;

    private java.math.BigDecimal paidAmount;

    private Integer checkIn;

    private LocalDateTime checkInTime;

    private LocalDateTime createdAt;
}
