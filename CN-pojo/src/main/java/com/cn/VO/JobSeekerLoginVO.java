package com.cn.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobSeekerLoginVO {
    private String token;
    private UserInfo user;

    /**
     * 求职者登录 VO
     *
     * @param token 代币
     * @param user  用户
     */
    public JobSeekerLoginVO(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }

    /**
     * 用户信息
     */
    @Data
    @AllArgsConstructor
    public static class UserInfo {
        private Long id;
        private String username;
        private String userType;
        private String email;
        private String phone;
        private String status;
        private LocalDateTime createdAt;
    }
}
