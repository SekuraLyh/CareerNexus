package com.cn.controller.auth;

import com.cn.DTO.JobQueryDTO;
import com.cn.VO.JobPostingVO;
import com.cn.VO.UserVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.JobsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "职位接口")
@Slf4j
@RequestMapping("/jobs")
public class JobsController {

    @Autowired
    private JobsService jobsService;

    @GetMapping
    @ApiOperation("首页预览最新职位")
    public Result<PageResult<JobPostingVO>> getJobPostings(JobQueryDTO jobQuery) {
        PageResult<JobPostingVO> result = jobsService.getJobs(jobQuery);
        return Result.success(result);
    }
}
