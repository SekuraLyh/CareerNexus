package com.cn.controller.admin;

import com.cn.VO.DashedBorderVO;
import com.cn.VO.SystemOverviewVO;
import com.cn.VO.UserVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/users")
    @ApiOperation("分页查询用户列表")
    public Result<PageResult<UserVO>> getUsers(
            @Parameter(description = "页码，从1开始", example = "1")
            @RequestParam(defaultValue = "1") Integer page,
            
            @Parameter(description = "每页大小", example = "10")
            @RequestParam(defaultValue = "10") Integer size,
            
            @Parameter(description = "用户类型（可选）", example = "JOB_SEEKER")
            @RequestParam(required = false) String userType,
            
            @Parameter(description = "账号状态（可选）", example = "ACTIVE")
            @RequestParam(required = false) String status,
            
            @Parameter(description = "搜索关键词（可选，支持用户名/邮箱/手机号）", example = "张三")
            @RequestParam(required = false) String keyword) {
        
        PageResult<UserVO> result = adminService.getUsers(page, size, userType, status, keyword);
        return Result.success(result);
    }
}
