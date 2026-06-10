package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("职位搜索请求")
public class JobSearchDTO {
    
    @ApiModelProperty(value = "关键词（匹配职位标题和描述）", example = "Java")
    private String keyword;
    
    @ApiModelProperty(value = "专业要求", example = "计算机科学与技术")
    private String major;
    
    @ApiModelProperty(value = "最低工作年限", example = "3")
    private Integer minExperience;
    
    @ApiModelProperty(value = "最高工作年限", example = "5")
    private Integer maxExperience;
    
    @ApiModelProperty(value = "最低薪资", example = "15000")
    private Integer minSalary;
    
    @ApiModelProperty(value = "最高薪资", example = "30000")
    private Integer maxSalary;
    
    @ApiModelProperty(value = "工作地点", example = "北京")
    private String location;
    
    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;
    
    @ApiModelProperty(value = "每页大小", example = "20")
    private Integer size = 20;
}
