package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教练响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class CoachResponse {

    private Long id;

    private String name;

    private Integer gender;

    private String phone;

    private String email;

    private String expertise;

    private String schedule;

    private LocalDateTime createdAt;
}
