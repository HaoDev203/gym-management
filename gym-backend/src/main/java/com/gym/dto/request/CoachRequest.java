package com.gym.dto.request;

import lombok.Data;

/**
 * 教练请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class CoachRequest {

    private String name;

    private Integer gender;

    private String phone;

    private String email;

    private String expertise;

    private String schedule;
}
