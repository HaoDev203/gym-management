package com.gym.controller;

import com.gym.common.BaseResponse;
import com.gym.dto.request.MemberLoginRequest;
import com.gym.dto.request.MemberRegisterRequest;
import com.gym.dto.response.MemberInfoResponse;
import com.gym.service.AuthService;
import com.gym.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证相关接口控制器。
 *
 * <p>提供会员注册、登录、登出、获取当前用户信息等接口。</p>
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    /**
     * 会员注册接口。
     *
     * @param request 注册请求体（包含用户名、密码、手机号等）
     * @return 统一返回结构，data 为 JWT token
     */
    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody @Valid MemberRegisterRequest request) {
        String token = authService.memberRegister(request);
        return BaseResponse.success(token);
    }

    /**
     * 会员登录接口。
     *
     * @param request 登录请求体（包含用户名、密码）
     * @return 统一返回结构，data 为 JWT token
     */
    @PostMapping("/login")
    public BaseResponse<String> login(@RequestBody @Valid MemberLoginRequest request) {
        String token = authService.memberLogin(request);
        return BaseResponse.success(token);
    }

    /**
     * 管理员登录接口。
     *
     * @param request 登录请求体（包含用户名、密码）
     * @return 统一返回结构，data 为 JWT token
     */
    @PostMapping("/admin/login")
    public BaseResponse<String> adminLogin(@RequestBody @Valid MemberLoginRequest request) {
        String token = authService.adminLogin(request);
        return BaseResponse.success(token);
    }
}
