package com.cn.controller.auth;

import com.cn.DTO.EnterpriseProfileDTO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.result.Result;
import com.cn.service.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@Slf4j
@Api(tags = "档案接口")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/enterprise")
    @ApiOperation("获取当前企业档案")
    public Result<EnterpriseProfileDTO> getEnterpriseProfile() {
        log.info("获取当前企业档案");
        EnterpriseProfileDTO profile = profileService.getEnterpriseProfile();
        
        if (profile == null) {
            return Result.error("档案不存在，请先创建档案");
        }
        
        return Result.success(profile);
    }

    @PutMapping("/enterprise")
    @ApiOperation("创建或更新企业档案")
    public Result<EnterpriseProfileDTO> saveEnterpriseProfile(@RequestBody EnterpriseProfileDTO dto) {
        log.info("创建或更新企业档案, companyName: {}", dto.getCompanyName());
        EnterpriseProfileDTO saved = profileService.saveEnterpriseProfile(dto);
        return Result.success(saved);
    }

    @GetMapping("/job-seeker")
    @ApiOperation("获取当前求职者档案")
    public Result<JobSeekerProfileDTO> getJobSeekerProfile() {
        log.info("获取当前求职者档案");
        JobSeekerProfileDTO profile = profileService.getJobSeekerProfile();
        
        if (profile == null) {
            return Result.error("档案不存在，请先创建档案");
        }
        
        return Result.success(profile);
    }

    @PutMapping("/job-seeker")
    @ApiOperation("创建或更新求职者档案")
    public Result<JobSeekerProfileDTO> saveJobSeekerProfile(@RequestBody JobSeekerProfileDTO dto) {
        log.info("创建或更新求职者档案, realName: {}", dto.getRealName());
        JobSeekerProfileDTO saved = profileService.saveJobSeekerProfile(dto);
        return Result.success(saved);
    }
}
