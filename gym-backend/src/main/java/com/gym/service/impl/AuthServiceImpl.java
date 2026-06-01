package com.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.dto.request.MemberLoginRequest;
import com.gym.dto.request.MemberRegisterRequest;
import com.gym.entity.Admin;
import com.gym.entity.Member;
import com.gym.mapper.AdminMapper;
import com.gym.mapper.MemberMapper;
import com.gym.service.AuthService;
import com.gym.util.JwtUtil;
import com.gym.util.RedisTokenStore;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 认证服务实现类。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberMapper memberMapper;

    private final AdminMapper adminMapper;

    private final PasswordEncoder passwordEncoder;

    private final RedisTokenStore redisTokenStore;

    @Override
    @Transactional
    public String memberRegister(MemberRegisterRequest request) {
        String phone = request.getPhone() != null ? request.getPhone() : request.getUsername();

        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getPhone, phone);
        if (memberMapper.selectCount(wrapper) > 0) {
            throw new BusinessException(ErrorCode.MEMBER_PHONE_EXIST);
        }

        Member member = new Member();
        member.setUsername(phone);
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setPhone(phone);
        member.setName(request.getName());
        member.setGender(request.getGender());
        member.setBirthday(request.getBirthday());
        member.setIdCard(request.getIdCard());
        member.setEmergencyContact(request.getEmergencyContact());
        member.setEmergencyPhone(request.getEmergencyPhone());
        member.setNoShowCount(0);
        member.setIsBanned(0);
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());
        memberMapper.insert(member);

        return JwtUtil.generateToken(member.getId(), "MEMBER");
    }

    @Override
    public String memberLogin(MemberLoginRequest request) {
        LambdaQueryWrapper<Member> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Member::getPhone, request.getUsername());
        Member member = memberMapper.selectOne(wrapper);
        if (member == null) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BusinessException(ErrorCode.MEMBER_PASSWORD_ERROR);
        }
        String token = JwtUtil.generateToken(member.getId(), "MEMBER");
        redisTokenStore.storeToken(member.getId(), token);
        return token;
    }

    @Override
    public String adminLogin(MemberLoginRequest request) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getUsername, request.getUsername());
        Admin admin = adminMapper.selectOne(wrapper);
        if (admin == null) {
            throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND);
        }
        if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
            throw new BusinessException(ErrorCode.ADMIN_PASSWORD_ERROR);
        }
        String token = JwtUtil.generateToken(admin.getId(), "ADMIN");
        redisTokenStore.storeToken(admin.getId(), token);
        return token;
    }
}
