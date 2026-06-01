package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.entity.Admin;
import com.gym.service.AdminService;
import com.gym.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理员控制器。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 获取当前登录管理员信息。
     *
     * @return 管理员信息
     */
    @GetMapping("/info")
    public BaseResponse<Admin> getCurrentAdmin(@RequestParam Long adminId) {
        Admin admin = adminService.getAdminById(adminId);
        return BaseResponse.success(admin);
    }

    @GetMapping("/{id}")
    public BaseResponse<?> getAdmin(@PathVariable Long id) {
        return BaseResponse.success(adminService.getAdminById(id));
    }

    @GetMapping("/list")
    public BaseResponse<List<Admin>> listAdmins() {
        return BaseResponse.success(adminService.listAllAdmins());
    }

    /**
     * 添加管理员（仅超级管理员可用）。
     *
     * @param admin 管理员信息
     * @return 新管理员 ID
     */
    @PostMapping("/add")
    public BaseResponse<Long> addAdmin(@RequestParam Long adminId, @RequestBody Admin admin) {
        // 检查权限
        Admin currentAdmin = adminService.getAdminById(adminId);
        PermissionUtil.checkSuperAdmin(currentAdmin);
        
        Admin created = adminService.createAdmin(admin);
        return BaseResponse.success(created.getId());
    }

    /**
     * 更新管理员（仅超级管理员可用）。
     *
     * @param id 管理员 ID
     * @param admin 管理员信息
     * @return 操作结果
     */
    @PutMapping("/{id}")
    public BaseResponse<Void> updateAdmin(@RequestParam Long adminId, @PathVariable Long id, @RequestBody Admin admin) {
        // 检查权限
        Admin currentAdmin = adminService.getAdminById(adminId);
        PermissionUtil.checkSuperAdmin(currentAdmin);
        
        admin.setId(id);
        adminService.createAdmin(admin);
        return BaseResponse.success(null);
    }

    /**
     * 删除管理员（仅超级管理员可用）。
     *
     * @param id 管理员 ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteAdmin(@RequestParam Long adminId, @PathVariable Long id) {
        // 检查权限
        Admin currentAdmin = adminService.getAdminById(adminId);
        PermissionUtil.checkSuperAdmin(currentAdmin);
        
        adminService.deleteAdmin(id);
        return BaseResponse.success(null);
    }
}
