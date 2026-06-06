package com.cn.controller.admin;

import com.cn.VO.DashedBorderVO;
import com.cn.result.Result;
import com.cn.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@Slf4j
@Api("管理员接口")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard")
    @ApiOperation("获取管理员仪表盘统计数据")
    public Result<DashedBorderVO> getDashboard() {
        DashedBorderVO dashedBorderVO = adminService.getDashedBorder();
        return Result.success(dashedBorderVO);
    }
}
