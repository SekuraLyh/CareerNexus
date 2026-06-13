package com.cn.service.impl;

import com.cn.DTO.ReportGenerateRequestDTO;
import com.cn.entity.DemandDistribution;
import com.cn.entity.IndustryReport;
import com.cn.entity.SalaryTrend;
import com.cn.exception.BusinessException;
import com.cn.mapper.ReportMapper;
import com.cn.result.PageResult;
import com.cn.service.ReportService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportMapper reportMapper;

    @Override
    public PageResult<IndustryReport> getReports(Integer page, Integer size, String industry) {
        log.info("分页查询报告列表: page={}, size={}, industry={}", page, size, industry);
        PageHelper.startPage(page, size);
        List<IndustryReport> reports = reportMapper.selectReports(industry);
        PageInfo<IndustryReport> pageInfo = new PageInfo<>(reports);
        return PageUtils.toPageResult(pageInfo);
    }

    @Override
    public IndustryReport getReportDetail(Long reportId) {
        log.info("获取报告详情: reportId={}", reportId);
        IndustryReport report = reportMapper.selectReportById(reportId);
        if (report == null) {
            throw new BusinessException(404, "报告不存在");
        }
        return report;
    }

    @Override
    public List<SalaryTrend> getSalaryTrends(String industry) {
        log.info("获取薪资趋势: industry={}", industry);
        return reportMapper.selectSalaryTrends(industry);
    }

    @Override
    public List<DemandDistribution> getDemandDistribution(String type) {
        log.info("获取需求分布: type={}", type);
        return reportMapper.selectDemandDistributions(type);
    }

    @Override
    public IndustryReport generateReport(ReportGenerateRequestDTO dto) {
        String industry = dto.getIndustry();
        log.info("生成行业报告: industry={}", industry);

        // 解析时间范围
        LocalDate periodStart = parseDate(dto.getPeriodStart(), LocalDate.now().minusMonths(1).withDayOfMonth(1));
        LocalDate periodEnd = parseDate(dto.getPeriodEnd(), LocalDate.now());

        // 1. 统计行业数据
        Integer jobCount = reportMapper.countJobsByIndustry(industry);
        Integer avgSalary = reportMapper.avgSalaryByIndustry(industry);

        if (jobCount == null || jobCount == 0) {
            throw new BusinessException(400, "该行业暂无有效职位数据，无法生成报告");
        }

        // 2. 生成标题和摘要
        String title = periodEnd.format(DateTimeFormatter.ofPattern("yyyy年M月")) + " " + industry + "行业就业分析报告";
        String summary = String.format("本月%s行业开放职位 %d 个，平均薪资约 ¥%,d/月",
                industry, jobCount, avgSalary != null ? avgSalary : 0);

        // 3. 生成报告正文（Markdown）
        String content = buildReportContent(industry, periodStart, periodEnd, jobCount, avgSalary);

        // 4. 保存报告
        IndustryReport report = new IndustryReport();
        report.setTitle(title);
        report.setIndustry(industry);
        report.setPeriodStart(periodStart);
        report.setPeriodEnd(periodEnd);
        report.setSummary(summary);
        report.setContent(content);
        report.setGeneratedAt(LocalDateTime.now());
        report.setCreatedAt(LocalDateTime.now());
        reportMapper.insertReport(report);

        // 5. 生成薪资趋势子数据（按月聚合）
        generateSalaryTrends(report.getId(), industry, periodStart, periodEnd);

        // 6. 生成需求分布子数据（按专业/地点/经验）
        generateDemandDistributions(report.getId(), industry);

        log.info("报告生成成功: id={}, title={}", report.getId(), title);
        return report;
    }

    @Override
    public void deleteReport(Long reportId) {
        log.info("删除报告: reportId={}", reportId);
        IndustryReport report = reportMapper.selectReportById(reportId);
        if (report == null) {
            throw new BusinessException(404, "报告不存在");
        }
        reportMapper.deleteReportById(reportId);
    }

    @Override
    public PageResult<IndustryReport> listAllReports(Integer page, Integer size, String industry) {
        return getReports(page, size, industry);
    }

    // ==================== 私有方法 ====================

    private String buildReportContent(String industry, LocalDate start, LocalDate end,
                                       int jobCount, Integer avgSalary) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return "# " + industry + "行业就业分析报告\n\n" +
                "## 基本信息\n\n" +
                "- **统计周期**: " + start.format(fmt) + " 至 " + end.format(fmt) + "\n" +
                "- **开放职位数**: " + jobCount + "\n" +
                "- **平均薪资**: ¥" + (avgSalary != null ? String.format("%,d", avgSalary) : "暂无数据") + "/月\n\n" +
                "## 行业概述\n\n" +
                industry + "行业作为国民经济的重要支柱之一，在人才市场上持续保持旺盛的招聘需求。" +
                "本报告综合分析该行业在统计周期内的职位发布情况、薪资水平变化以及人才需求分布，" +
                "为求职者和企业提供数据参考。\n\n" +
                "## 薪资分析\n\n" +
                "统计周期内，该行业开放职位的平均月薪约为 ¥" +
                (avgSalary != null ? String.format("%,d", avgSalary) : "暂无数据") + "。" +
                "薪资水平受地区、经验要求和岗位类型等多重因素影响，建议结合需求分布数据综合判断。\n\n" +
                "## 需求趋势\n\n" +
                "从需求分布来看，该行业对不同专业背景、工作经验的人才均有一定需求。" +
                "求职者可根据自身条件，参考需求分布数据选择合适的发展方向。\n\n" +
                "## 求职建议\n\n" +
                "1. 关注行业动态，及时了解最新的招聘趋势和技能要求\n" +
                "2. 根据薪资趋势数据，合理设定薪资期望\n" +
                "3. 针对需求较大的专业/技能方向进行有针对性的提升\n\n" +
                "---\n*本报告由 CareerNexus 自动生成，数据来源于平台职位发布信息。*";
    }

    private void generateSalaryTrends(Long reportId, String industry, LocalDate start, LocalDate end) {
        // 按月生成薪资趋势
        List<SalaryTrend> trends = new ArrayList<>();
        LocalDate current = start.withDayOfMonth(1);
        while (!current.isAfter(end)) {
            String period = current.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            // 简化：使用平均薪资和总职位数作为当前月的趋势点
            Integer jobCount = reportMapper.countJobsByIndustry(industry);
            Integer avgSalary = reportMapper.avgSalaryByIndustry(industry);

            if (jobCount != null && jobCount > 0) {
                SalaryTrend trend = new SalaryTrend();
                trend.setReportId(reportId);
                trend.setPeriod(period);
                trend.setAvgSalary(avgSalary != null ? avgSalary : 0);
                trend.setJobCount(jobCount);
                trends.add(trend);
            }
            current = current.plusMonths(1);
        }
        if (!trends.isEmpty()) {
            reportMapper.insertSalaryTrends(trends);
        }
    }

    private void generateDemandDistributions(Long reportId, String industry) {
        List<DemandDistribution> distributions = new ArrayList<>();

        // 按专业分布 (MAJOR) — 从 job_postings.required_major 聚合
        distributions.addAll(buildDistribution(reportId, "MAJOR", industry, "required_major"));

        // 按地点分布 (LOCATION) — 从 job_postings.location 聚合
        distributions.addAll(buildDistribution(reportId, "LOCATION", industry, "location"));

        // 按经验分布 (EXPERIENCE) — 从 job_postings 经验区间聚合
        distributions.addAll(buildExperienceDistribution(reportId, industry));

        if (!distributions.isEmpty()) {
            reportMapper.insertDemandDistributions(distributions);
        }
    }

    private List<DemandDistribution> buildDistribution(Long reportId, String dimensionType,
                                                        String industry, String column) {
        // 从 job_postings JOIN enterprise_profiles 获取数据
        List<Map<String, Object>> rows = fetchDistributionData(industry, column);
        int total = rows.stream().mapToInt(r -> ((Number) r.get("cnt")).intValue()).sum();
        if (total == 0) return Collections.emptyList();

        return rows.stream().map(row -> {
            DemandDistribution dd = new DemandDistribution();
            dd.setReportId(reportId);
            dd.setDimensionType(dimensionType);
            Object labelObj = row.get("label");
            dd.setLabel(labelObj != null ? labelObj.toString() : "其他");
            int cnt = ((Number) row.get("cnt")).intValue();
            dd.setCount(cnt);
            dd.setPercentage(BigDecimal.valueOf(cnt * 100.0 / total).setScale(2, RoundingMode.HALF_UP));
            return dd;
        }).collect(Collectors.toList());
    }

    private List<DemandDistribution> buildExperienceDistribution(Long reportId, String industry) {
        // 从 job_postings 经验区间预聚合
        List<Map<String, Object>> rows = fetchExperienceData(industry);
        int total = rows.stream().mapToInt(r -> ((Number) r.get("cnt")).intValue()).sum();
        if (total == 0) return Collections.emptyList();

        return rows.stream().map(row -> {
            DemandDistribution dd = new DemandDistribution();
            dd.setReportId(reportId);
            dd.setDimensionType("EXPERIENCE");
            dd.setLabel(row.get("label").toString());
            int cnt = ((Number) row.get("cnt")).intValue();
            dd.setCount(cnt);
            dd.setPercentage(BigDecimal.valueOf(cnt * 100.0 / total).setScale(2, RoundingMode.HALF_UP));
            return dd;
        }).collect(Collectors.toList());
    }

    // 注意：这些聚合查询需要动态SQL，改为直接在Service中用Mapper执行
    private List<Map<String, Object>> fetchDistributionData(String industry, String column) {
        // 使用 Mapper 内置方法，改为调用新的聚合查询
        // 这里为简化，直接返回空列表，实际由数据库查询实现
        return reportMapper.selectDistribution(industry, column);
    }

    private List<Map<String, Object>> fetchExperienceData(String industry) {
        return reportMapper.selectExperienceDistribution(industry);
    }

    private LocalDate parseDate(String dateStr, LocalDate defaultDate) {
        if (dateStr == null || dateStr.isEmpty()) return defaultDate;
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return defaultDate;
        }
    }
}
