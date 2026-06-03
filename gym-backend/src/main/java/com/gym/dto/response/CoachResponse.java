package com.gym.dto.response;

import com.gym.util.DesensitizationUtil;
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

    /**
     * 获取脱敏后的手机号。
     *
     * @return 脱敏后的手机号
     */
    public String getPhone() {
        return DesensitizationUtil.desensitizePhone(phone);
    }

    /**
     * 获取脱敏后的姓名。
     *
     * @return 脱敏后的姓名
     */
    public String getName() {
        return DesensitizationUtil.desensitizeName(name);
    }

    /**
     * 获取脱敏后的邮箱。
     *
     * @return 脱敏后的邮箱
     */
    public String getEmail() {
        return DesensitizationUtil.desensitizeEmail(email);
    }
}
