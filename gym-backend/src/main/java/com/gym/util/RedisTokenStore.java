package com.gym.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis Token 存储工具类。
 *
 * <p>用于实现账号互斥登录，存储每个用户的最新 Token。</p>
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Slf4j
@Component
public class RedisTokenStore {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static final String TOKEN_PREFIX = "user:token:";
    private static final long TOKEN_EXPIRATION_HOURS = 24;

    /**
     * 存储用户 Token。
     *
     * @param userId 用户 ID
     * @param token JWT Token
     */
    public void storeToken(Long userId, String token) {
        try {
            String key = getTokenKey(userId);
            redisTemplate.opsForValue().set(key, token, TOKEN_EXPIRATION_HOURS, TimeUnit.HOURS);
            log.debug("Token 已存储到 Redis: userId={}, key={}", userId, key);
        } catch (Exception e) {
            log.error("存储 Token 到 Redis 失败：userId={}", userId, e);
        }
    }

    /**
     * 获取用户存储的 Token。
     *
     * @param userId 用户 ID
     * @return 存储的 Token，如果不存在则返回 null
     */
    public String getToken(Long userId) {
        try {
            String key = getTokenKey(userId);
            String token = redisTemplate.opsForValue().get(key);
            log.debug("从 Redis 获取 Token: userId={}, key={}, token={}", userId, key, token != null ? "exists" : "null");
            return token;
        } catch (Exception e) {
            log.error("从 Redis 获取 Token 失败：userId={}", userId, e);
            return null;
        }
    }

    /**
     * 删除用户 Token。
     *
     * @param userId 用户 ID
     */
    public void deleteToken(Long userId) {
        try {
            String key = getTokenKey(userId);
            redisTemplate.delete(key);
            log.debug("Token 已从 Redis 删除：userId={}", userId);
        } catch (Exception e) {
            log.error("从 Redis 删除 Token 失败：userId={}", userId, e);
        }
    }

    /**
     * 验证 Token 是否为当前最新 Token。
     *
     * @param userId 用户 ID
     * @param token 要验证的 Token
     * @return true 表示 Token 有效且为最新，false 表示 Token 已失效（账号在其他地方登录）
     */
    public boolean validateToken(Long userId, String token) {
        try {
            String storedToken = getToken(userId);
            if (storedToken == null) {
                log.warn("Redis 中未找到 Token，认为 Token 有效（降级处理）: userId={}", userId);
                return true;
            }
            boolean valid = token.equals(storedToken);
            if (!valid) {
                log.warn("Token 验证失败：账号已在其他地方登录 userId={}", userId);
            }
            return valid;
        } catch (Exception e) {
            log.error("验证 Token 失败：userId={}", userId, e);
            return true;
        }
    }

    private String getTokenKey(Long userId) {
        return TOKEN_PREFIX + userId;
    }
}
