package com.cn.service;

import com.cn.DTO.JobSearchDTO;
import com.cn.DTO.JobSeekerSearchDTO;
import com.cn.VO.JobPostingVO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.result.PageResult;

public interface SearchService {

    /**
     * 模糊搜索职位
     *
     * @param searchDTO 搜索条件
     * @return 分页职位列表
     */
    PageResult<JobPostingVO> searchJobs(JobSearchDTO searchDTO);

    /**
     * 搜索求职者档案
     *
     * @param searchDTO 搜索条件
     * @return 分页求职者档案列表
     */
    PageResult<JobSeekerProfileDTO> searchJobSeekers(JobSeekerSearchDTO searchDTO);
}
