package com.gym.enums;

import lombok.Getter;

/**
 * 课程类型枚举。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Getter
public enum CourseType {

    GROUP(1, "团课"),

    PRIVATE(2, "私教课");

    private final int code;

    private final String desc;

    CourseType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
