package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("职位状态请求")
public class JobStatusDTO {

    @NotBlank(message = "状态不能为空")
    private String status;
}
