package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 器材实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("equipment")
public class Equipment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer quantity;

    private String location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
