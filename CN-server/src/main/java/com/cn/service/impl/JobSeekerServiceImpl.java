package com.cn.service.impl;

import com.cn.DTO.RegisterJobSeekerDTO;
import com.cn.service.JobSeekerService;
import com.cn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobSeekerServiceImpl implements JobSeekerService {
    
    @Autowired
    private UserService userService;


    @Override
    public void registerJobSeeker(RegisterJobSeekerDTO dto) {
        // 调用通用用户注册服务
        Long userId = userService.registerUser(
            dto.getUsername(), 
            dto.getPassword(),
            dto.getEmail(), 
            dto.getPhone(), 
            "JOB_SEEKER"
        );
        
        // TODO: 如果需要，这里可以创建 job_seeker_profiles 记录
//         jobSeekerMapper.insert(...)
        
        log.info("求职者档案创建完成, 用户ID: {}", userId);
    }
}
