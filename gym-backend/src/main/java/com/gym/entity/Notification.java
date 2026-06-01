package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 通知实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("notification")
public class Notification {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long memberId;

    private String title;

    private String content;

    private Integer type;

    private Integer isRead;

    private Integer isDeleted;

    private LocalDateTime createdAt;

    /**
     * 判断通知是否已读。
     *
     * @return true 表示已读
     */
    public boolean isRead() {
        return this.isRead != null && this.isRead == 1;
    }

    /**
     * 标记为已读。
     */
    public void markAsRead() {
        this.isRead = 1;
    }
}
