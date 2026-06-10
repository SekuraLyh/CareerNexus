package com.cn.service;

import com.cn.DTO.JobQueryDTO;
import com.cn.VO.JobPostingVO;
import com.cn.result.PageResult;

public interface JobsService {
    PageResult<JobPostingVO> getJobs(JobQueryDTO jobQuery);
}
