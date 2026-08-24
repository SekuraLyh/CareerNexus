package com.cn.VO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "用户登录响应")
public class LoginVO {

    @Schema(description = "JWT 令牌")
    private String token;

    @Schema(description = "用户信息")
    private UserInfo user;

    public LoginVO(String token, UserInfo user) {
        this.token = token;
        this.user = user;
    }

    @Data
    @AllArgsConstructor
    @Schema(description = "用户基本信息")
    public static class UserInfo {

        @Schema(description = "用户ID")
        private Long id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "用户类型（jobseeker/enterprise/admin）")
        private String userType;

        @Schema(description = "邮箱")
        private String email;

        @Schema(description = "手机号")
        private String phone;

        @Schema(description = "账号状态（active/disabled）")
        private String status;

        @Schema(description = "注册时间")
        private LocalDateTime createdAt;
    }
}
