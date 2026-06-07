package com.cn.mapper;

import com.cn.DTO.EnterpriseProfileDTO;
import com.cn.DTO.JobSeekerProfileDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProfileMapper {

    /**
     * 根据用户ID查询企业档案
     *
     * @param userId 用户ID
     * @return 企业档案 DTO，不存在返回 null
     */
    EnterpriseProfileDTO getEnterpriseProfileById(@Param("userId") Long userId);

    /**
     * 插入企业档案
     *
     * @param dto 企业档案 DTO
     * @return 影响行数
     */
    int insertEnterpriseProfile(EnterpriseProfileDTO dto);

    /**
     * 根据档案ID更新企业档案（动态更新）
     *
     * @param dto 企业档案 DTO
     * @return 影响行数
     */
    int updateEnterpriseProfile(EnterpriseProfileDTO dto);

    /**
     * 根据用户ID查询求职者档案
     *
     * @param userId 用户ID
     * @return 求职者档案 DTO，不存在返回 null
     */
    JobSeekerProfileDTO getJobSeekerProfileById(@Param("userId") Long userId);

    /**
     * 插入求职者档案
     *
     * @param dto 求职者档案 DTO
     * @return 影响行数
     */
    int insertJobSeekerProfile(JobSeekerProfileDTO dto);

    /**
     * 根据档案ID更新求职者档案（动态更新）
     *
     * @param dto 求职者档案 DTO
     * @return 影响行数
     */
    int updateJobSeekerProfile(JobSeekerProfileDTO dto);
}
