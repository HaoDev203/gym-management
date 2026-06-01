package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员简要信息响应 DTO（用于展示列表）。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberInfoResponse {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    private Integer gender;

    private LocalDate birthday;

    private Integer noShowCount;

    private Integer isBanned;

    private LocalDateTime createdAt;
}
