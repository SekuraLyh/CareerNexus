package com.cn.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class JobPostDTO {

    @NotBlank(message = "职位标题不能为空")
    private String title;

    @NotBlank(message = "职位描述不能为空")
    private String description;

    private String requiredMajor;

    private Integer minExperience;

    private Integer maxExperience;

    private Integer minSalary;

    private Integer maxSalary;

    @NotBlank(message = "工作地点不能为空")
    private String location;
}
