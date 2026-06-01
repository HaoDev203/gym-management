package com.gym.enums;

import lombok.Getter;

/**
 * 候补状态枚举。
 *
 * @author liuxinsi
 * @date 2026-05-23
 */
@Getter
public enum WaitlistStatus {

    QUEUING(0, "排队中"),
    PROMOTED(1, "已转正"),
    EXPIRED(2, "已过期");

    private final int code;
    private final String desc;

    WaitlistStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
