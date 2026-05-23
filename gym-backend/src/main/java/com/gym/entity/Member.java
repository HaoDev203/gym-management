package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 会员实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("member")
public class Member {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String phone;

    private String email;

    private Integer age;

    private String idCard;

    private Integer gender;

    private LocalDate birthday;

    private String emergencyContact;

    private String emergencyPhone;

    private Integer noShowCount = 0;

    private Integer isBanned = 0;

    private LocalDateTime bannedUntil;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 增加爽约次数。
     */
    public void incrementNoShowCount() {
        if (this.noShowCount == null) {
            this.noShowCount = 0;
        }
        this.noShowCount++;
    }

    /**
     * 清空爽约次数。
     */
    public void clearNoShowCount() {
        this.noShowCount = 0;
    }

    /**
     * 判断会员当前是否处于限制预约状态。
     *
     * @return true 表示被限制预约
     */
    public boolean isBanned() {
        if (this.isBanned == null || this.isBanned == 0) {
            return false;
        }
        if (this.bannedUntil == null) {
            return true;
        }
        return !LocalDateTime.now().isAfter(this.bannedUntil);
    }
}
