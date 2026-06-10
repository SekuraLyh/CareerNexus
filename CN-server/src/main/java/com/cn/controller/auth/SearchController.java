package com.cn.controller.auth;

import com.cn.DTO.JobSearchDTO;
import com.cn.DTO.JobSeekerSearchDTO;
import com.cn.VO.JobPostingVO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Slf4j
@Api(tags = "搜索接口")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/jobs")
    @ApiOperation("模糊搜索职位")
    public Result<PageResult<JobPostingVO>> searchJobs(JobSearchDTO searchDTO) {
        log.info("搜索职位, keyword: {}, major: {}, location: {}", 
                searchDTO.getKeyword(), searchDTO.getMajor(), searchDTO.getLocation());
        PageResult<JobPostingVO> result = searchService.searchJobs(searchDTO);
        return Result.success(result);
    }

    @GetMapping("/job-seekers")
    @ApiOperation("搜索求职者")
    public Result<PageResult<JobSeekerProfileDTO>> searchJobSeekers(JobSeekerSearchDTO searchDTO) {
        log.info("搜索求职者, major: {}, education: {}, skills: {}", 
                searchDTO.getMajor(), searchDTO.getEducation(), searchDTO.getSkills());
        PageResult<JobSeekerProfileDTO> result = searchService.searchJobSeekers(searchDTO);
        return Result.success(result);
    }
}
