package com.cn.mapper;

import com.cn.entity.DemandDistribution;
import com.cn.entity.IndustryReport;
import com.cn.entity.SalaryTrend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    /** 分页查询报告列表，支持行业筛选 */
    List<IndustryReport> selectReports(@Param("industry") String industry);

    /** 根据ID查询报告详情 */
    IndustryReport selectReportById(@Param("reportId") Long reportId);

    /** 插入报告 */
    void insertReport(IndustryReport report);

    /** 根据ID删除报告 */
    void deleteReportById(@Param("reportId") Long reportId);

    /** 根据行业查询薪资趋势（跨报告聚合） */
    List<SalaryTrend> selectSalaryTrends(@Param("industry") String industry);

    /** 根据维度类型查询需求分布（跨报告聚合） */
    List<DemandDistribution> selectDemandDistributions(@Param("dimensionType") String dimensionType);

    /** 批量插入薪资趋势数据 */
    void insertSalaryTrends(@Param("list") List<SalaryTrend> trends);

    /** 批量插入需求分布数据 */
    void insertDemandDistributions(@Param("list") List<DemandDistribution> distributions);

    /** 按行业统计职位数量 */
    Integer countJobsByIndustry(@Param("industry") String industry);

    /** 按行业统计平均薪资 */
    Integer avgSalaryByIndustry(@Param("industry") String industry);

    /** 通用分布数据聚合（按列名 GROUP BY） */
    List<Map<String, Object>> selectDistribution(@Param("industry") String industry, @Param("column") String column);

    /** 经验分布数据聚合（按经验区间 GROUP BY） */
    List<Map<String, Object>> selectExperienceDistribution(@Param("industry") String industry);
}
