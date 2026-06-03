package com.gym.dto.response;

import com.gym.util.DesensitizationUtil;
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

    /**
     * 获取脱敏后的手机号。
     *
     * @return 脱敏后的手机号
     */
    public String getPhone() {
        return DesensitizationUtil.desensitizePhone(phone);
    }

    /**
     * 获取脱敏后的身份证号。
     *
     * @return 脱敏后的身份证号
     */
    public String getIdCard() {
        return DesensitizationUtil.desensitizeIdCard(idCard);
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

    /**
     * 获取脱敏后的紧急联系人电话。
     *
     * @return 脱敏后的紧急联系人电话
     */
    public String getEmergencyPhone() {
        return DesensitizationUtil.desensitizeEmergencyPhone(emergencyPhone);
    }
}
