package com.gym.enums;

import lombok.Getter;

/**
 * 通知类型枚举。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Getter
public enum NotificationType {

    BOOK_SUCCESS(1, "预约成功"),

    CANCEL_NOTICE(2, "取消通知"),

    PRE_CLASS_REMINDER(3, "课前提醒"),

    COURSE_STARTING(4, "即将开始"),

    WAITLIST_SUCCESS(5, "候补成功"),

    COURSE_CANCELLED(6, "课程取消"),

    COURSE_REMINDER(7, "课程提醒"),

    SYSTEM_NOTICE(8, "系统通知");

    private final int code;

    private final String desc;

    NotificationType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getValue() {
        return this.code;
    }
}
