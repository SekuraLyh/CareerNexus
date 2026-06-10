package com.cn.service.impl;

import com.cn.DTO.JobSearchDTO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.DTO.JobSeekerSearchDTO;
import com.cn.VO.JobPostingVO;
import com.cn.entity.JobPosting;
import com.cn.mapper.JobsMapper;
import com.cn.mapper.ProfileMapper;
import com.cn.result.PageResult;
import com.cn.service.SearchService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private JobsMapper jobsMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public PageResult<JobPostingVO> searchJobs(JobSearchDTO searchDTO) {
        // 1. 设置分页参数
        PageHelper.startPage(searchDTO.getPage(), searchDTO.getSize());

        // 2. 执行搜索
        List<JobPosting> jobs = jobsMapper.searchJobs(
                searchDTO.getKeyword(),
                searchDTO.getMajor(),
                searchDTO.getMinExperience(),
                searchDTO.getMaxExperience(),
                searchDTO.getMinSalary(),
                searchDTO.getMaxSalary(),
                searchDTO.getLocation()
        );

        // 3. 封装分页结果并转换为 VO
        PageInfo<JobPosting> pageInfo = new PageInfo<>(jobs);
        return PageUtils.toPageResult(pageInfo, job -> {
            JobPostingVO vo = new JobPostingVO();
            BeanUtils.copyProperties(job, vo);
            return vo;
        });
    }

    @Override
    public PageResult<JobSeekerProfileDTO> searchJobSeekers(JobSeekerSearchDTO searchDTO) {
        // 1. 设置分页参数
        PageHelper.startPage(searchDTO.getPage(), searchDTO.getSize());

        // 2. 执行搜索
        List<JobSeekerProfileDTO> seekers = profileMapper.searchJobSeekers(
                searchDTO.getMajor(),
                searchDTO.getEducation(),
                searchDTO.getMinExperience(),
                searchDTO.getSkills()
        );

        // 3. 封装分页结果
        PageInfo<JobSeekerProfileDTO> pageInfo = new PageInfo<>(seekers);
        return PageUtils.toPageResult(pageInfo);
    }
}
