package com.gym.dto.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 管理员添加请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-27
 */
@Data
public class AdminAddRequest {

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 角色（1-超级管理员，2-普通管理员）
     */
    private Integer role;
}
