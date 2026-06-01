package com.gym.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger / Knife4j API 文档配置类。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@Configuration
public class SwaggerConfig {

    /**
     * 配置 OpenAPI 文档信息及 JWT 认证。
     *
     * @return OpenAPI 实例
     */
    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("Authorization");

        return new OpenAPI()
                .info(new Info()
                        .title("健身房管理系统 API 文档")
                        .version("1.0.0")
                        .description("健身房管理系统后端接口文档")
                        .contact(new Contact().name("liuxinsi")))
                .addSecurityItem(securityRequirement)
                .schemaRequirement("Authorization", securityScheme);
    }
}
