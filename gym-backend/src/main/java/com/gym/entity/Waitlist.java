package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 候补排队实体类。
 *
 * @author liuxinsi
 * @date 2026-05-23
 */
@Data
@TableName("waitlist")
public class Waitlist {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private Long courseId;

    private Integer status;

    private Integer queuePosition;

    private LocalDateTime createdAt;

    public boolean isQueuing() {
        return this.status != null && this.status == 0;
    }
}
