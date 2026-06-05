package com.cn.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT 工具类：生成和解析 JWT 令牌
 */
public class JwtUtil {

    /**
     * 生成 JWT
     * 使用 HS256 算法，私钥使用固定秘钥
     *
     * @param secretKey JWT 秘钥
     * @param ttlMillis JWT 过期时间（毫秒）
     * @param claims    设置的信息
     * @return JWT 字符串
     */
    public static String createJWT(String secretKey, long ttlMillis, Map<String, Object> claims) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date exp = new Date(expMillis);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .signWith(signatureAlgorithm, secretKey.getBytes(StandardCharsets.UTF_8))
                .setExpiration(exp);

        return builder.compact();
    }

    /**
     * Token 解密
     *
     * @param secretKey JWT 秘钥（必须与服务端保持一致）
     * @param token     加密后的 token
     * @return Claims 对象
     */
    public static Claims parseJWT(String secretKey, String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }

}
