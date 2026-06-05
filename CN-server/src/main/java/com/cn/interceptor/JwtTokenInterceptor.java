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
 * JWT 令牌校验拦截器 — 拦截除登录/注册/首页外所有请求。
 * <p>
 * 白名单配置见 {@code WebMvcConfiguration.addInterceptors()}
 */
@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 非 Controller 方法放行（如静态资源、OPTIONS 预检）
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 1. 从请求头中获取令牌（支持 Authorization / Authorization: Bearer <token> 两种格式）
        String token = request.getHeader(jwtProperties.getAdminTokenName());
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 2. 无令牌 → 401
        if (token == null || token.isEmpty()) {
            log.warn("JWT 令牌缺失: {}", request.getRequestURI());
            writeUnauthorized(response, "未登录，请先登录");
            return false;
        }

        // 3. 校验令牌
        try {
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("当前用户 ID：{}", userId);
            BaseContext.setCurrentId(userId);
            return true;
        } catch (Exception ex) {
            log.warn("JWT 校验失败: {}", ex.getMessage());
            writeUnauthorized(response, "登录已过期，请重新登录");
            return false;
        }
    }

    /**
     * 向客户端写入 401 JSON 响应
     */
    private void writeUnauthorized(HttpServletResponse response, String message) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":0,\"msg\":\"" + message + "\",\"data\":null}");
    }
}
