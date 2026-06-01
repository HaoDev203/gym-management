package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class CourseResponse {

    private Long id;

    private String name;

    private Integer type;

    private Long coachId;

    private String coachName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer capacity;

    private Integer bookedCount;

    private java.math.BigDecimal price;

    private Integer status;

    private LocalDateTime createdAt;

    private Boolean isExpired;
}
