package com.cn.service;

import com.cn.DTO.EnterpriseProfileDTO;
import com.cn.DTO.JobSeekerProfileDTO;

public interface ProfileService {

    /**
     * 获取当前用户的企业档案
     *
     * @return 企业档案 DTO，不存在返回 null
     */
    EnterpriseProfileDTO getEnterpriseProfile();

    /**
     * 创建或更新企业档案
     *
     * @param dto 企业档案数据传输对象
     * @return 保存后的企业档案 DTO
     */
    EnterpriseProfileDTO saveEnterpriseProfile(EnterpriseProfileDTO dto);

    /**
     * 获取当前用户的求职者档案
     *
     * @return 求职者档案 DTO，不存在返回 null
     */
    JobSeekerProfileDTO getJobSeekerProfile();

    /**
     * 创建或更新求职者档案
     *
     * @param dto 求职者档案数据传输对象
     * @return 保存后的求职者档案 DTO
     */
    JobSeekerProfileDTO saveJobSeekerProfile(JobSeekerProfileDTO dto);

    /**
     * 根据用户 ID 获取公开档案
     *
     * @param userId 用户 ID
     * @return 公开档案 DTO，不存在返回 null
     */
    JobSeekerProfileDTO getPublicProfile(Long userId);
}
