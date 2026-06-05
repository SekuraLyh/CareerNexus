package com.cn.service;

import com.cn.DTO.RegisterJobSeekerDTO;

public interface JobSeekerService {

    /**
     * 注册求职者
     * 注意：登录功能已移至 UserService，由所有用户类型共用
     */
    void registerJobSeeker(RegisterJobSeekerDTO registerJobSeekerDTO);
}
