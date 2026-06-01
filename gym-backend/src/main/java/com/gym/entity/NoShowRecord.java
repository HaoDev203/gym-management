package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 爽约记录实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("no_show_record")
public class NoShowRecord {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Long orderId;

    private LocalDateTime courseTime;

    private LocalDateTime createdAt;
}
