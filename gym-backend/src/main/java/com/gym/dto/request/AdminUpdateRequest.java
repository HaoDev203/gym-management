package com.gym.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 管理员更新请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-27
 */
@Data
public class AdminUpdateRequest {

    /**
     * 管理员 ID
     */
    @NotNull(message = "管理员 ID 不能为空")
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 角色（1-超级管理员，2-普通管理员）
     */
    private Integer role;
}
