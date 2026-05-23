package com.gym.service;

import com.gym.dto.request.MemberLoginRequest;
import com.gym.dto.request.MemberRegisterRequest;

/**
 * 认证服务接口。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
public interface AuthService {

    /**
     * 会员注册。
     *
     * @param request 注册请求
     * @return JWT Token
     */
    String memberRegister(MemberRegisterRequest request);

    /**
     * 会员登录。
     *
     * @param request 登录请求
     * @return JWT Token
     */
    String memberLogin(MemberLoginRequest request);

    /**
     * 管理员登录。
     *
     * @param request 登录请求
     * @return JWT Token
     */
    String adminLogin(MemberLoginRequest request);
}
