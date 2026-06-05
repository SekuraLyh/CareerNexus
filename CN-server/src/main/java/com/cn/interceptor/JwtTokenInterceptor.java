package com.cn.interceptor;

import com.cn.constant.JwtClaimsConstant;
import com.cn.context.BaseContext;
import com.cn.properties.JwtProperties;
import com.cn.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT 令牌校验拦截器（管理端）
 * <p>
 * 使用方式：
 * 1. 配置 WebMvcConfiguration 注册此拦截器，指定拦截路径和排除路径
 * 2. 在 application.yml 中配置 cn.jwt 相关属性
 * 3. 登录成功后生成 JWT 令牌返回给前端，后续请求在 Header 中携带令牌
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("JWT 拦截器处理请求：{}", request.getRequestURI());

        // 判断当前拦截到的是 Controller 的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 1. 从请求头中获取令牌（支持 Authorization: Bearer <token> 格式）
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 2. 校验令牌
        try {
            log.info("JWT 校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户 ID：{}", userId);
            BaseContext.setCurrentId(userId);
            return true;
        } catch (Exception ex) {
            log.error("JWT 校验失败", ex);
            // 不通过，响应 401 状态码
            response.setStatus(401);
            return false;
        }
    }
}
