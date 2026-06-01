package com.gym.util;

import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.entity.Admin;
import com.gym.enums.AdminRole;

/**
 * 权限检查工具类。
 *
 * @author liuxinsi
 * @date 2026-06-01
 */
public class PermissionUtil {

    private PermissionUtil() {
    }

    /**
     * 检查是否为超级管理员。
     *
     * @param admin 管理员对象
     * @throws BusinessException 如果不是超级管理员
     */
    public static void checkSuperAdmin(Admin admin) {
        if (admin == null) {
            throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND);
        }
        if (!admin.isSuperAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "权限不足，仅超级管理员可用");
        }
    }

    /**
     * 检查是否有管理员权限。
     *
     * @param admin 管理员对象
     * @throws BusinessException 如果没有权限
     */
    public static void checkAdminPermission(Admin admin) {
        if (admin == null) {
            throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND);
        }
        if (!admin.hasPermission()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "权限不足");
        }
    }
}
