package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.AdminAddRequest;
import com.gym.dto.request.AdminUpdateRequest;
import com.gym.entity.Admin;
import com.gym.service.AdminService;
import jakarta.validation.Valid;
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
    public BaseResponse<Long> addAdmin(@RequestBody @Valid AdminAddRequest request) {
        Admin admin = new Admin();
        admin.setUsername(request.getUsername());
        admin.setPassword(request.getPassword());
        admin.setName(request.getName());
        admin.setRole(request.getRole());
        Admin created = adminService.createAdmin(admin);
        return BaseResponse.success(created.getId());
    }

    @PutMapping("/{id}")
    public BaseResponse<Void> updateAdmin(@PathVariable Long id, @RequestBody @Valid AdminUpdateRequest request) {
        Admin admin = adminService.getAdminById(id);
        if (admin == null) {
            return BaseResponse.error(404, "管理员不存在");
        }
        if (request.getName() != null) {
            admin.setName(request.getName());
        }
        if (request.getRole() != null) {
            admin.setRole(request.getRole());
        }
        adminService.createAdmin(admin);
        return BaseResponse.success(null);
    }

    @DeleteMapping("/{id}")
    public BaseResponse<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return BaseResponse.success(null);
    }
}
