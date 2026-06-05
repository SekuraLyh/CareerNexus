package com.cn.controller.auth;

import com.cn.DTO.ChangePasswordDTO;
import com.cn.DTO.LoginDTO;
import com.cn.DTO.RegisterEnterpriseDTO;
import com.cn.DTO.RegisterJobSeekerDTO;
import com.cn.VO.LoginVO;
import com.cn.context.BaseContext;
import com.cn.result.Result;
import com.cn.service.EnterpriseService;
import com.cn.service.JobSeekerService;
import com.cn.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/auth")
@Api(tags = "用户认证接口")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JobSeekerService jobSeekerService;
    
    @Autowired
    private EnterpriseService enterpriseService;



    /**
     *
     * @param loginDTO 登录信息
     * @return {@link Result< LoginVO >}
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功"),
            @ApiResponse(responseCode = "401", description = "用户名或密码错误")
    })
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        log.info("用户登录: {}", loginDTO.getUsername());
        LoginVO loginVO = userService.login(loginDTO);
        return Result.success(loginVO);
    }

    /**
     * 修改密码
     *
     * @param passwordDTO 密码信息（包含旧密码和新密码）
     * @return {@link Result}
     */
    @PutMapping("/password")
    @Operation(summary = "修改密码")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "密码修改成功"),
            @ApiResponse(responseCode = "400", description = "旧密码错误"),
            @ApiResponse(responseCode = "401", description = "未登录或Token过期")
    })
    public Result changePassword(@RequestBody ChangePasswordDTO passwordDTO) {
        // 从 JWT Token 中获取当前用户ID（由拦截器设置）
        Long userId = BaseContext.getCurrentId();
        log.info("用户修改密码: userId={}", userId);
        
        userService.changePassword(userId, passwordDTO);
        
        return Result.success("密码修改成功");
    }


    /**
     * 注册求职者
     *
     * @param registerJobSeekerDTO 注册求职者 DTO
     * @return {@link Result}
     */
    @PostMapping("/register/job-seeker")
    @Operation(summary = "注册求职者")

    public Result registerJobSeeker(@RequestBody RegisterJobSeekerDTO registerJobSeekerDTO) {

        jobSeekerService.registerJobSeeker(registerJobSeekerDTO);

        return Result.success("注册成功");
    }

    /**
     * 注册企业
     *
     * @param registerEnterpriseDTO 注册企业 DTO
     * @return {@link Result}
     */
    @PostMapping("/register/enterprise")
    @Operation(summary = "注册企业")
    public Result registerEnterprise(@RequestBody RegisterEnterpriseDTO registerEnterpriseDTO) {

        enterpriseService.registerEnterprise(registerEnterpriseDTO);

        return Result.success("注册成功");
    }
}
