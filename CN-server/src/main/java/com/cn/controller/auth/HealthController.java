package com.cn.controller.auth;

import com.cn.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查 / 示例控制器
 */
@RestController
@RequestMapping("/admin/health")
@Slf4j
@Api(tags = "健康检查接口")
public class HealthController {

    @GetMapping
    @ApiOperation("服务健康检查")
    public Result<String> health() {
        log.info("健康检查...");
        return Result.success("CareerNexus server is running");
    }

}
