package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.entity.Admin;
import com.gym.service.AdminService;
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

    @GetMapping("/{id}")
    public BaseResponse<?> getAdmin(@PathVariable Long id) {
        return BaseResponse.success(adminService.getAdminById(id));
    }

    @GetMapping("/list")
    public BaseResponse<List<Admin>> listAdmins() {
        return BaseResponse.success(adminService.listAllAdmins());
    }

    @PostMapping("/add")
    public BaseResponse<Long> addAdmin(@RequestBody Admin admin) {
        Admin created = adminService.createAdmin(admin);
        return BaseResponse.success(created.getId());
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        admin.setId(id);
        adminService.createAdmin(admin);
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return BaseResponse.success(null);
    }
}
