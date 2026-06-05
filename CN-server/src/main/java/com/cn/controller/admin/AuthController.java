package com.cn.controller.admin;

import com.cn.DTO.JobSeekerLoginDTO;
import com.cn.VO.JobSeekerLoginVO;
import com.cn.result.Result;
import com.cn.service.JobSeekerService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@Api(tags = "认证接口")
public class AuthController {

    @Autowired
    private JobSeekerService jobSeekerService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody JobSeekerLoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());

        JobSeekerLoginVO loginVO = jobSeekerService.login(loginDTO);

        if (loginVO != null) {
            return Result.success(loginVO);
        }

        return Result.error("用户名或密码错误");
    }
}
