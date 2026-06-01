package com.gym.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gym.enums.AdminRole;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 管理员实体类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
@TableName("admin")
public class Admin {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    private Integer role;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * 判断是否为超级管理员。
     *
     * @return true 表示超级管理员
     */
    public boolean isSuperAdmin() {
        return this.role != null && this.role == AdminRole.SUPER_ADMIN.getCode();
    }

    /**
     * 判断是否为普通管理员。
     *
     * @return true 表示普通管理员
     */
    public boolean isNormalAdmin() {
        return this.role != null && this.role == AdminRole.NORMAL.getCode();
    }

    /**
     * 判断是否有操作权限。
     *
     * @return true 表示有权限
     */
    public boolean hasPermission() {
        return this.role != null;
    }
}
