package com.cn.service.impl;

import com.cn.DTO.EnterpriseProfileDTO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.context.BaseContext;
import com.cn.mapper.ProfileMapper;
import com.cn.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public EnterpriseProfileDTO getEnterpriseProfile() {
        Long userId = BaseContext.getCurrentId();
        return profileMapper.getEnterpriseProfileById(userId);
    }

    @Override
    public EnterpriseProfileDTO saveEnterpriseProfile(EnterpriseProfileDTO dto) {
        Long userId = BaseContext.getCurrentId();
        
        // 1. 查询是否已存在档案
        EnterpriseProfileDTO existing = profileMapper.getEnterpriseProfileById(userId);
        
        if (existing != null) {
            // 2. 更新现有档案
            dto.setId(existing.getId());
            dto.setUserId(existing.getUserId());
            profileMapper.updateEnterpriseProfile(dto);
            log.info("更新企业档案成功, userId: {}, companyName: {}", userId, dto.getCompanyName());
        } else {
            // 3. 创建新档案
            dto.setUserId(userId.intValue());
            profileMapper.insertEnterpriseProfile(dto);
            log.info("创建企业档案成功, userId: {}, companyName: {}", userId, dto.getCompanyName());
        }
        
        // 4. 返回最新档案
        return profileMapper.getEnterpriseProfileById(userId);
    }

    @Override
    public JobSeekerProfileDTO getJobSeekerProfile() {
        Long userId = BaseContext.getCurrentId();
        return profileMapper.getJobSeekerProfileById(userId);
    }

    @Override
    public JobSeekerProfileDTO saveJobSeekerProfile(JobSeekerProfileDTO dto) {
        Long userId = BaseContext.getCurrentId();
        
        // 1. 查询是否已存在档案
        JobSeekerProfileDTO existing = profileMapper.getJobSeekerProfileById(userId);
        
        if (existing != null) {
            // 2. 更新现有档案
            dto.setId(existing.getId());
            dto.setUserId(existing.getUserId());
            profileMapper.updateJobSeekerProfile(dto);
            log.info("更新求职者档案成功, userId: {}, realName: {}", userId, dto.getRealName());
        } else {
            // 3. 创建新档案
            dto.setUserId(userId.intValue());
            profileMapper.insertJobSeekerProfile(dto);
            log.info("创建求职者档案成功, userId: {}, realName: {}", userId, dto.getRealName());
        }
        
        // 4. 返回最新档案
        return profileMapper.getJobSeekerProfileById(userId);
    }

    @Override
    public JobSeekerProfileDTO getPublicProfile(Long userId) {
        return profileMapper.getJobSeekerProfileById(userId);
    }
}
