package com.gym.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 器材响应 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class EquipmentResponse {

    private Long id;

    private String name;

    private Integer quantity;

    private String location;

    private LocalDateTime createdAt;
}
