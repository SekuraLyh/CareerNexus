package com.cn.service;

import com.cn.DTO.JobPostDTO;
import com.cn.DTO.JobQueryDTO;
import com.cn.VO.JobPostingVO;
import com.cn.result.PageResult;

public interface JobsService {
    PageResult<JobPostingVO> getJobs(JobQueryDTO jobQuery);

    JobPostingVO createJob(JobPostDTO jobPostDTO);


    JobPostingVO getJobDetail(Integer jobId);

    JobPostingVO updateJob(Integer jobId, JobPostDTO jobPostDTO);

    PageResult<JobPostingVO> getMyJobs(Integer page, Integer size, String status);
}
