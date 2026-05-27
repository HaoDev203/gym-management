package com.gym.util;

import com.gym.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Slf4j
@Component
public class JwtUtil {

    private static JwtConfig jwtConfig;

    @Autowired
    public void setJwtConfig(JwtConfig jwtConfig) {
        JwtUtil.jwtConfig = jwtConfig;
    }

    private static SecretKey getSecretKey() {
        String secret = jwtConfig.getSecret();
        if (secret == null || secret.isEmpty()) {
            log.warn("JWT secret is not configured, using default secret");
            secret = "gym-management-system-secret-key-2026";
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static long getExpiration() {
        return jwtConfig.getExpiration() != null ? jwtConfig.getExpiration() : 86400000L;
    }

    private JwtUtil() {
    }

    /**
     * 生成 JWT Token。
     *
     * @param userId 用户 ID
     * @param role   用户角色
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + getExpiration());

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSecretKey())
                .compact();
    }

    /**
     * 从 Token 中解析 Claims。
     *
     * @param token JWT Token
     * @return Claims 对象
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证 Token 是否有效。
     *
     * @param token JWT Token
     * @return true 表示有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 中解析用户 ID 并验证。
     *
     * @param token JWT Token
     * @return 用户 ID，如果 token 无效则返回 null
     */
    public Long parseUserId(String token) {
        if (!validateToken(token)) {
            return null;
        }
        return getUserId(token);
    }

    /**
     * 从 Token 中获取用户 ID。
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public static Long getUserId(String token) {
        Claims claims = parseToken(token);
        return Long.valueOf(claims.getSubject());
    }

    /**
     * 从 Token 中获取用户 ID（别名方法）。
     *
     * @param token JWT Token
     * @return 用户 ID
     */
    public static Long getUserIdFromToken(String token) {
        return getUserId(token);
    }

    /**
     * 从 Token 中获取用户角色。
     *
     * @param token JWT Token
     * @return 用户角色字符串
     */
    public static String getUserRole(String token) {
        Claims claims = parseToken(token);
        return claims.get("role", String.class);
    }
}
