package com.cn.controller.admin;

import com.cn.DTO.JobSeekerLoginDTO;
import com.cn.VO.JobSeekerLoginVO;
import com.cn.result.Result;
import com.cn.service.JobSeekerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@Tag(name = "认证接口", description = "用户登录与身份认证")
public class AuthController {

    @Autowired
    private JobSeekerService jobSeekerService;

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "401", description = "用户名或密码错误")
    })
    public Result<JobSeekerLoginVO> login(@RequestBody JobSeekerLoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());
        JobSeekerLoginVO loginVO = jobSeekerService.login(loginDTO);
        return Result.success(loginVO);
    }
}
