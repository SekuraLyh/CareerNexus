package com.cn.service;

import com.cn.DTO.ChangePasswordDTO;
import com.cn.DTO.LoginDTO;
import com.cn.VO.LoginVO;

public interface UserService {

    /**
     * 登录
     *
     * @param loginDTO 登录 DTO
     * @return {@link LoginVO}
     */
    LoginVO login(LoginDTO loginDTO);

    /**
     * 修改密码
     *
     * @param userId   用户ID（从 JWT Token 中获取）
     * @param passwordDTO 密码信息（包含旧密码和新密码）
     */
    void changePassword(Long userId, ChangePasswordDTO passwordDTO);

    /**
     * 注册用户
     *
     * @param username 用户名
     * @param password 密码
     * @param email    电子邮件
     * @param phone    电话
     * @param userType 用户类型
     * @return {@link Long}
     */
    Long registerUser(String username, String password, String email, String phone, String userType);

}
