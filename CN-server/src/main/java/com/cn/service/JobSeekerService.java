package com.cn.service;

import com.cn.DTO.JobSeekerLoginDTO;
import com.cn.VO.JobSeekerLoginVO;

public interface JobSeekerService {

    JobSeekerLoginVO login(JobSeekerLoginDTO loginDTO);

}
