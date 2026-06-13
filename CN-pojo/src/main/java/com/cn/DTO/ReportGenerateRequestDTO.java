package com.cn.DTO;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ReportGenerateRequestDTO {
    @NotBlank(message = "行业名称不能为空")
    private String industry;

    private String periodStart;

    private String periodEnd;
}
