package com.gym.util;

import com.gym.common.BusinessException;
import com.gym.common.ErrorCode;
import com.gym.entity.Admin;
import com.gym.entity.Member;
import com.gym.mapper.AdminMapper;
import com.gym.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 安全工具类。
 *
 * <p>用途：从 SecurityContext 中获取当前登录用户信息。</p>
 *
 * @author liuxinsi
 * @date 2026-06-08
 */
@Component
public final class SecurityUtil {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private MemberMapper memberMapper;

    private SecurityUtil() {
    }

    /**
     * 获取当前登录用户的 UserDetails。
     *
     * @return UserDetails 对象
     * @throws BusinessException 如果用户未登录
     */
    public static UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        
        Object principal = authentication.getPrincipal();
        
        if (!(principal instanceof UserDetails)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户信息格式错误");
        }
        
        return (UserDetails) principal;
    }

    /**
     * 获取当前登录用户的 ID。
     *
     * @return 用户 ID
     * @throws BusinessException 如果用户未登录
     */
    public static Long getCurrentUserId() {
        UserDetails userDetails = getCurrentUserDetails();
        String username = userDetails.getUsername();
        
        // 尝试从 Admin 表查询
        Admin admin = getAdminMapper().selectByUsername(username);
        if (admin != null) {
            return admin.getId();
        }
        
        // 尝试从 Member 表查询
        Member member = getMemberMapper().selectByUsername(username);
        if (member != null) {
            return member.getId();
        }
        
        throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户不存在：" + username);
    }

    private static AdminMapper getAdminMapper() {
        return SpringUtil.getBean(AdminMapper.class);
    }

    private static MemberMapper getMemberMapper() {
        return SpringUtil.getBean(MemberMapper.class);
    }

    /**
     * 获取当前登录的会员 ID。
     *
     * @return 会员 ID
     * @throws BusinessException 如果用户未登录或不是会员角色
     */
    public static Long getCurrentMemberId() {
        UserDetails userDetails = getCurrentUserDetails();
        
        if (!userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MEMBER"))) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "需要会员权限");
        }
        
        return getCurrentUserId();
    }

    /**
     * 获取当前登录的管理员 ID。
     *
     * @return 管理员 ID
     * @throws BusinessException 如果用户未登录或不是管理员角色
     */
    public static Long getCurrentAdminId() {
        UserDetails userDetails = getCurrentUserDetails();
        
        // 检查是否是管理员角色（包括普通管理员和超级管理员）
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN") 
                        || auth.getAuthority().equals("ROLE_SUPER_ADMIN"));
        
        if (!isAdmin) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "需要管理员权限");
        }
        
        return getCurrentUserId();
    }
}
