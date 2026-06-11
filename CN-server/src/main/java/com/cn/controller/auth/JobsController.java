package com.cn.controller.auth;

import com.cn.DTO.JobPostDTO;
import com.cn.DTO.JobQueryDTO;
import com.cn.VO.JobPostingVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.JobsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping
    @ApiOperation("发布职位")
    public Result<JobPostingVO> jobPost(@RequestBody JobPostDTO jobPostDTO) {
        return Result.success(jobsService.createJob(jobPostDTO));
    }

    @GetMapping("/my")
    @ApiOperation("获取我发布的职位列表")
    public Result<PageResult<JobPostingVO>> getMyJobs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String status) {
        PageResult<JobPostingVO> result = jobsService.getMyJobs(page, size, status);
        return Result.success(result);
    }

    @GetMapping("/{jobId}")
    @ApiOperation("获取职位详情")
    public Result<JobPostingVO> getJobDetail(@PathVariable Integer jobId) {
        return Result.success(jobsService.getJobDetail(jobId));
    }

    @PutMapping("/{jobId:}")
    @ApiOperation("更新职位信息")
    public Result<JobPostingVO> updateJob(@PathVariable Integer jobId, @RequestBody @Valid JobPostDTO jobPostDTO) {
        return Result.success(jobsService.updateJob(jobId, jobPostDTO));
    }
}
