package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class NotificationResponse {

    private Long id;

    private String title;

    private String content;

    private Integer type;

    private Integer isRead;

    private LocalDateTime createdAt;
}
