package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("求职者搜索请求")
public class JobSeekerSearchDTO {
    
    @ApiModelProperty(value = "专业", example = "计算机科学与技术")
    private String major;
    
    @ApiModelProperty(value = "学历", example = "BACHELOR")
    private String education;
    
    @ApiModelProperty(value = "最低工作年限", example = "3")
    private Integer minExperience;
    
    @ApiModelProperty(value = "技能关键词", example = "Java,Spring")
    private String skills;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;
    
    @ApiModelProperty(value = "每页大小", example = "20")
    private Integer size = 20;
}
