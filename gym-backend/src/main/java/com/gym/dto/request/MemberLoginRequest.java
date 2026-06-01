package com.gym.dto.request;

import lombok.Data;

/**
 * 会员登录请求 DTO。
 *
 * @author liuxinsi
 * @date 2026-05-21
 */
@Data
public class MemberLoginRequest {

    private String username;

    private String password;
}
