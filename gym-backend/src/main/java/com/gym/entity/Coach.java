package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 教练实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("coach")
public class Coach {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer gender;

    private String phone;

    private String email;

    private String expertise;

    private String schedule;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
