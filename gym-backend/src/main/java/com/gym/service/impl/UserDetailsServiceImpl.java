package com.gym.service.impl;

import com.gym.entity.Admin;
import com.gym.entity.Member;
import com.gym.enums.AdminRole;
import com.gym.mapper.AdminMapper;
import com.gym.mapper.MemberMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务实现
 * 
 * <p>用途：加载用户信息供 Spring Security 使用。</p>
 * 
 * @author AI Assistant
 * @date 2026-05-21
 */
@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("加载用户信息：username={}", username);
        
        Member member = memberMapper.selectByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("用户不存在：" + username);
        }
        
        return new User(
            member.getUsername(),
            member.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER"))
        );
    }

    /**
     * 根据用户 ID 加载用户详情，同时支持管理员和会员。
     * 
     * @param userId 用户 ID
     * @return 用户详情
     * @throws UsernameNotFoundException 用户不存在时抛出
     */
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        log.debug("加载用户信息：userId={}", userId);

        Admin admin = adminMapper.selectById(userId);
        if (admin != null) {
            String role = admin.isSuperAdmin() ? "ROLE_SUPER_ADMIN" : "ROLE_ADMIN";
            return new User(
                admin.getUsername(),
                admin.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
            );
        }

        Member member = memberMapper.selectById(userId);
        if (member == null) {
            throw new UsernameNotFoundException("用户不存在：" + userId);
        }
        
        return new User(
            member.getUsername(),
            member.getPassword(),
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER"))
        );
    }
}
