package com.cn.entity;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DemandDistribution {
    private Long id;
    private Long reportId;
    private String dimensionType;
    private String label;
    private Integer count;
    private BigDecimal percentage;
}
