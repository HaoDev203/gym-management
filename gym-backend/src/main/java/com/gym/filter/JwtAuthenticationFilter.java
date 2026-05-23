package com.gym.filter;

import com.gym.service.impl.UserDetailsServiceImpl;
import com.gym.util.JwtUtil;
import com.gym.util.RedisTokenStore;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器
 * 
 * <p>用途：解析请求中的 JWT 令牌，验证用户身份并设置安全上下文。</p>
 * 
 * @author AI Assistant
 * @date 2026-05-21
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private RedisTokenStore redisTokenStore;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt)) {
                Long userId = JwtUtil.getUserIdFromToken(jwt);
                
                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserById(userId);
                    
                    if (JwtUtil.validateToken(jwt)) {
                        // 暂时禁用 Redis Token 验证，允许同时登录
                        // if (!redisTokenStore.validateToken(userId, jwt)) {
                        //     log.warn("Token 验证失败：账号 {} 已在其他地方登录", userId);
                        //     response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        //     response.setContentType("application/json;charset=UTF-8");
                        //     response.getWriter().write("{\"code\":401,\"message\":\"该账号已在其他地方登录\"}");
                        //     return;
                        // }
                        
                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                            );
                        
                        authentication.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        log.debug("已设置用户身份：userId={}", userId);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT 认证失败：{}", e.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }

    /**
     * 从请求头中获取 JWT 令牌
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        
        return null;
    }
}
