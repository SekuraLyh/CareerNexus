package com.cn.controller.admin;

import com.cn.DTO.UserQueryDTO;
import com.cn.DTO.UserStatusRequestDTO;
import com.cn.VO.DashedBorderVO;
import com.cn.VO.SystemOverviewVO;
import com.cn.VO.UserVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api(tags = "管理员接口")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    @ApiOperation("获取管理员仪表盘统计数据")
    public Result<DashedBorderVO> getDashboard() {
        DashedBorderVO dashedBorderVO = adminService.getDashedBorder();
        return Result.success(dashedBorderVO);
    }

    @GetMapping("/overview")
    @ApiOperation("获取系统概览")
    public Result<SystemOverviewVO> getSystemOverview() {

        return Result.success(adminService.getSystemOverview());
    }


    @Operation(summary = "获取用户", method = "GET")
    @GetMapping("/users")
    public Result<PageResult<UserVO>> getUsers(UserQueryDTO userQuery) {
        
        PageResult<UserVO> result = adminService.getUsers(
                userQuery.getPage(),
                userQuery.getSize(),
                userQuery.getUserType(),
                userQuery.getStatus(),
                userQuery.getKeyword()
        );
        return Result.success(result);
    }


    @GetMapping("/users/{userId}")
    @ApiOperation("获取用户详情")
    public Result<UserVO> getUserDetail(@PathVariable Long userId) {
        log.info("获取用户详情: userId={}", userId);
        UserVO userVO = adminService.getUserDetail(userId);
        return Result.success(userVO);
    }

    @PutMapping("/users/{userId}")
    @ApiOperation("启用/禁用用户")
    public Result updateUserStatus(@PathVariable Long userId, @RequestBody UserStatusRequestDTO request) {
        log.info("更新用户状态: userId={}, status={}", userId, request.getStatus());
        adminService.updateUserStatus(userId, request.getStatus());
        return Result.success();
    }
}
