package com.cn.service.impl;

import com.cn.DTO.JobQueryDTO;
import com.cn.VO.JobPostingVO;
import com.cn.VO.UserVO;
import com.cn.entity.JobPosting;
import com.cn.entity.UserAccount;
import com.cn.mapper.JobsMapper;
import com.cn.result.PageResult;
import com.cn.service.JobsService;
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
public class JobsServiceImpl implements JobsService {
    @Autowired
    private JobsMapper josMapper;
    @Override
    public PageResult<JobPostingVO> getJobs(JobQueryDTO jobQuery) {
        // 1. 设置分页参数（必须在查询之前调用）
        PageHelper.startPage(jobQuery.getPage(), jobQuery.getSize());

        // 2. 执行查询（优先使用组合条件查询，支持灵活筛选）
        List<JobPosting> jobs = josMapper.selectJosByConditions(
                jobQuery.getMajor(), 
                jobQuery.getKeyword(), 
                jobQuery.getMinSalary(), 
                jobQuery.getLocation());

        // 3. 封装为 PageInfo（包含总数、总页数等信息）
        PageInfo<JobPosting> pageInfo = new PageInfo<>(jobs);

        // 4. 使用 PageUtils 工具类转换（Entity -> VO）
        PageResult<JobPostingVO> result = PageUtils.toPageResult(pageInfo, this::convertToJobPostingVO);
        return result;
    }

    /**
     * Entity 转 VO（提取为独立方法，便于复用和维护）
     */
    private JobPostingVO convertToJobPostingVO(JobPosting job) {
        JobPostingVO vo = new JobPostingVO();
        BeanUtils.copyProperties(job, vo);
        return vo;
    }
}
