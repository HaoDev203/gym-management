package com.gym.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 课程创建/更新请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class CourseRequest {

    private String name;

    private Integer type;

    private Long coachId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer capacity;

    private BigDecimal price;
}
