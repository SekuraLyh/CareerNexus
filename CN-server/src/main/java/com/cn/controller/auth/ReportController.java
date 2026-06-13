package com.cn.controller.auth;

import com.cn.DTO.ReportGenerateRequestDTO;
import com.cn.entity.DemandDistribution;
import com.cn.entity.IndustryReport;
import com.cn.entity.SalaryTrend;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.ReportService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reports")
@Slf4j
@Api(tags = "行业报告接口")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping
    public Result<PageResult<IndustryReport>> getReports(
            @RequestParam(required = false) String industry,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        log.info("获取报告列表: industry={}, page={}, size={}", industry, page, size);
        PageResult<IndustryReport> result = reportService.getReports(page, size, industry);
        return Result.success(result);
    }

    @GetMapping("/salary-trends")
    public Result<List<SalaryTrend>> getSalaryTrends(
            @RequestParam(required = false) String industry,
            @RequestParam(required = false) String major) {
        log.info("获取薪资趋势: industry={}, major={}", industry, major);
        List<SalaryTrend> trends = reportService.getSalaryTrends(industry);
        return Result.success(trends);
    }

    @GetMapping("/demand-distribution")
    public Result<List<DemandDistribution>> getDemandDistribution(
            @RequestParam(defaultValue = "MAJOR") String type) {
        log.info("获取需求分布: type={}", type);
        List<DemandDistribution> distributions = reportService.getDemandDistribution(type);
        return Result.success(distributions);
    }

    @GetMapping("/{reportId}")
    public Result<IndustryReport> getReportDetail(@PathVariable Long reportId) {
        log.info("获取报告详情: reportId={}", reportId);
        IndustryReport report = reportService.getReportDetail(reportId);
        return Result.success(report);
    }

    @PostMapping("/generate")
    public Result<IndustryReport> generateReport(@Valid @RequestBody ReportGenerateRequestDTO dto) {
        log.info("生成行业报告: industry={}", dto.getIndustry());
        IndustryReport report = reportService.generateReport(dto);
        return Result.success(report);
    }
}
