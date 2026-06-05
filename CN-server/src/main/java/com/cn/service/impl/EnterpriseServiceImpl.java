package com.cn.service.impl;

import com.cn.DTO.RegisterEnterpriseDTO;
import com.cn.service.EnterpriseService;
import com.cn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EnterpriseServiceImpl implements EnterpriseService {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void registerEnterprise(RegisterEnterpriseDTO dto) {
        // 调用通用用户注册服务
        Long userId = userService.registerUser(
            dto.getUsername(), 
            dto.getPassword(),
            dto.getEmail(), 
            dto.getPhone(), 
            "ENTERPRISE"
        );
        
        // TODO: 如果需要，这里可以创建 enterprise_profiles 记录
        // enterpriseProfileMapper.insert(...)
        
        log.info("企业档案创建完成, 用户ID: {}, 公司名称: {}", userId, dto.getCompanyName());
    }
}
