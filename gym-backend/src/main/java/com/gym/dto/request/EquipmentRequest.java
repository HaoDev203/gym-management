package com.gym.dto.request;

import lombok.Data;

/**
 * 器材请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class EquipmentRequest {

    private String name;

    private Integer quantity;

    private String location;
}
