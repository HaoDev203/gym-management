package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员信息响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberResponse {

    private Long id;

    private String username;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    private Integer gender;

    private LocalDate birthday;

    private String idCard;

    private String emergencyContact;

    private String emergencyPhone;

    private Integer noShowCount;

    private Integer isBanned;

    private LocalDateTime bannedUntil;

    private LocalDateTime createdAt;
}
