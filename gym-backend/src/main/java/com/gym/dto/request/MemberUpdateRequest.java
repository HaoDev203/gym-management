package com.gym.dto.request;

import lombok.Data;

import java.time.LocalDate;

/**
 * 会员信息更新请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberUpdateRequest {

    private Long id;

    private String name;

    private Integer gender;

    private LocalDate birthday;

    private String idCard;

    private String emergencyContact;

    private String emergencyPhone;

    private String phone;

    private String email;

    private Integer age;
}
