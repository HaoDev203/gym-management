package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.entity.Admin;
import com.gym.enums.AdminRole;
import com.gym.mapper.AdminMapper;
import com.gym.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 管理员管理服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Admin createAdmin(Admin admin) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, admin.getUsername());
        if (adminMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.ADMIN_USERNAME_EXIST);
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if (admin.getRole() == null) {
            admin.setRole(AdminRole.NORMAL.getCode());
        }
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminMapper.insert(admin);
        return admin;
    }

    @Override
    @Transactional
    public Admin updateAdmin(Long id, Admin admin) {
        Admin existing = adminMapper.selectById(id);
        if (existing == null) {
            throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND);
        }
        if (existing.isSuperAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "超级管理员信息不可修改");
        }
        existing.setName(admin.getName());
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            existing.setPassword(passwordEncoder.encode(admin.getPassword()));
        }
        existing.setUpdatedAt(LocalDateTime.now());
        adminMapper.updateById(existing);
        return existing;
    }

    @Override
    public Admin getAdminById(Long id) {
        Admin admin = adminMapper.selectById(id);
        if (admin == null) {
            throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND);
        }
        return admin;
    }

    @Override
    public List<Admin> listAllAdmins() {
        return adminMapper.selectList(null);
    }

    @Override
    @Transactional
    public void deleteAdmin(Long id) {
        Admin admin = adminMapper.selectById(id);
        if (admin != null && admin.isSuperAdmin()) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "超级管理员不可删除");
        }
        adminMapper.deleteById(id);
    }
}
