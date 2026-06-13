package com.cn.service;

import com.cn.DTO.ReportGenerateRequestDTO;
import com.cn.entity.DemandDistribution;
import com.cn.entity.IndustryReport;
import com.cn.entity.SalaryTrend;
import com.cn.result.PageResult;

import java.util.List;

public interface ReportService {

    /** 分页获取行业报告列表 */
    PageResult<IndustryReport> getReports(Integer page, Integer size, String industry);

    /** 获取报告详情 */
    IndustryReport getReportDetail(Long reportId);

    /** 获取薪资趋势数据 */
    List<SalaryTrend> getSalaryTrends(String industry);

    /** 获取需求分布数据 */
    List<DemandDistribution> getDemandDistribution(String type);

    /** 触发生成行业报告 */
    IndustryReport generateReport(ReportGenerateRequestDTO dto);

    /** 删除报告（管理员） */
    void deleteReport(Long reportId);

    /** 分页获取所有报告（管理员） */
    PageResult<IndustryReport> listAllReports(Integer page, Integer size, String industry);
}
