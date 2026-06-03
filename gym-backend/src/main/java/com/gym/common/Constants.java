package com.gym.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 系统常量定义类。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Component
public final class Constants {

    private static String jwtSecret;
    private static long jwtExpiration;

    private Constants() {
    }

    @Value("${jwt.secret}")
    public void setJwtSecret(String secret) {
        Constants.jwtSecret = secret;
    }

    @Value("${jwt.expiration}")
    public void setJwtExpiration(long expiration) {
        Constants.jwtExpiration = expiration;
    }

    public static String getJwtSecret() {
        return jwtSecret != null ? jwtSecret : JWT_SECRET;
    }

    public static long getJwtExpiration() {
        return jwtExpiration > 0 ? jwtExpiration : JWT_EXPIRATION;
    }

    // 兼容旧代码
    public static final String JWT_SECRET = "gym-management-system-secret-key-2026";
    public static final long JWT_EXPIRATION = 86400000L;

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
