package com.gym.common;

/**
 * 系统常量定义类。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
public final class Constants {

    private Constants() {
    }

    public static final int NO_SHOW_THRESHOLD = 3;

    public static final int BAN_DAYS = 7;

    public static final int MAX_COURSE_CAPACITY = 50;

    public static final int COURSE_CHECK_IN_MINUTES = 15;

    public static final int COURSE_CANCEL_HOURS = 2;

    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String REDIS_TOKEN_PREFIX = "token:";

    public static final String REDIS_BLACKLIST_PREFIX = "blacklist:";
}
