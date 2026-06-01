package com.gym.enums;

import lombok.Getter;

/**
 * 管理员角色枚举。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Getter
public enum AdminRole {

    NORMAL(1, "普通管理员"),

    SUPER_ADMIN(2, "超级管理员");

    private final int code;

    private final String desc;

    AdminRole(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
