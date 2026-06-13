package com.cn.entity;

import lombok.Data;

@Data
public class SalaryTrend {
    private Long id;
    private Long reportId;
    private String period;
    private Integer avgSalary;
    private Integer jobCount;
}
