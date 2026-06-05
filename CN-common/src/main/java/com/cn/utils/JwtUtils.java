package com.cn.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Map;

/**
 * JWT 实用程序
 *
 * @author ZhuanZ（无密码）
 * @date 2026/06/04 22:04
 */
public class JwtUtils {

    private static final String SIGN_KEY = "CareerNexus@2024SecretKey!JwtToken"; // JWT密钥
    private static final Long EXPIRE = 43200000L; // 12 hours

    /**
     * 生成JWT令牌
     */
    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SIGN_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
    }

    /**
     * 解析JWT令牌
     */
    public static Claims parseJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(SIGN_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
}
