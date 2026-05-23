package com.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 健身房管理系统主应用类。
 *
 * @author liuxinsi
 * @date 2026-05-20
 */
@SpringBootApplication
@EnableScheduling
public class GymApplication {

    /**
     * Spring Boot 应用入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(GymApplication.class, args);
    }
}
