package com.cn.service.impl;

import com.cn.DTO.ChangePasswordDTO;
import com.cn.DTO.LoginDTO;
import com.cn.VO.LoginVO;
import com.cn.VO.LoginVO.UserInfo;
import com.cn.entity.UserAccont;
import com.cn.exception.BusinessException;
import com.cn.exception.LoginFailedException;
import com.cn.mapper.UserMapper;
import com.cn.properties.JwtProperties;
import com.cn.service.UserService;
import com.cn.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.cn.constant.MessageConstant.*;
import static com.cn.constant.StatusConstant.ACTIVE;

/**
 * 通用用户服务实现类
 * 处理所有用户类型的公共业务逻辑（登录、注册）
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtProperties jwtProperties;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // 密码编码器

    /**
     * 登录
     *
     * @param loginDTO 登录 DTO
     * @return {@link LoginVO}
     */
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        
        // 查询用户
        UserAccont user = userMapper.getByUsername(username);
        
        // 验证用户和密码
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new LoginFailedException(PASSWORD_ERROR);
        }
        
        // 构建 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("userType", user.getUserType());
        
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims
        );
        
        // 构建用户信息
        UserInfo userInfo = new UserInfo(
                user.getId(),
                user.getUsername(),
                user.getUserType(),
                user.getEmail(),
                user.getPhone(),
                user.getStatus(),
                user.getCreatedAt()
        );
        
        log.info("用户登录成功: {}, 类型: {}", username, user.getUserType());
        return new LoginVO(token, userInfo);
    }

    /**
     * 修改密码
     *
     * @param userId      用户ID（从 JWT Token 中获取）
     * @param passwordDTO 密码信息（包含旧密码和新密码）
     */
    @Override
    public void changePassword(Long userId, ChangePasswordDTO passwordDTO) {
        // 查询用户
        UserAccont user = userMapper.getById(userId);
        if (user == null) {
            throw new BusinessException(404, ACCOUNT_NOT_FOUND);
        }

        // 验证旧密码
        if (!passwordEncoder.matches(passwordDTO.getOldPassword(), user.getPassword())) {
            throw new BusinessException(400, OLD_PASSWORD_ERROR);
        }

        // 更新为新密码
        String newPasswordEncoded = passwordEncoder.encode(passwordDTO.getNewPassword());
        userMapper.updatePassword(userId, newPasswordEncoded);

        log.info("用户密码修改成功: userId={}, username={}", userId, user.getUsername());
    }

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
    @Override
    public Long registerUser(String username, String password, 
                             String email, String phone, String userType) {
        // 验证用户名唯一性
        if (userMapper.getByUsername(username) != null) {
            throw new BusinessException(409, ALREADY_EXIST);
        }
        
        // 验证邮箱唯一性
        if (userMapper.getByEmail(email) != null) {
            throw new BusinessException(409, EMAIL_ALREADY_REGISTERED);
        }
        
        // 创建用户记录
        UserAccont user = new UserAccont();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setPhone(phone);
        user.setUserType(userType);
        user.setStatus(ACTIVE);
        
        userMapper.insert(user);
        
        log.info("{} 注册成功: {}", userType, username);
        
        return user.getId();
    }
}
