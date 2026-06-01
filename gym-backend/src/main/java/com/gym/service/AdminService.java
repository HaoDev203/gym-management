package com.gym.service;

import com.gym.entity.Admin;

import java.util.List;

/**
 * 管理员管理服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface AdminService {

    Admin createAdmin(Admin admin);

    Admin getAdminById(Long id);

    List<Admin> listAllAdmins();

    void deleteAdmin(Long id);
}
