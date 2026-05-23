package com.gym.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDate;

/**
 * 会员注册请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberRegisterRequest {

    private String username;

    private String password;

    private String name;

    private String phone;

    private Integer gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private String idCard;

    private String emergencyContact;

    private String emergencyPhone;
}
