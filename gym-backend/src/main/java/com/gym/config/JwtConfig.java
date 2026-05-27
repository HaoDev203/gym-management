package com.gym.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置类。
 *
 * <p>用于从配置文件中读取 JWT 相关配置。</p>
 *
 * @author liuxinsi
 * @date 2026-05-27
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    /**
     * JWT 密钥
     */
    private String secret;

    /**
     * JWT 过期时间（毫秒）
     */
    private Long expiration;
}
