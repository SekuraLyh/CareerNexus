package com.cn.VO;

import lombok.Data;

@Data
public class JobPostingVO {
    private Integer id;
    private Integer enterpriseUserId;
    private String enterpriseName;
    private String title;
    private String description;
    private String requiredMajor;
    private String minExperience;
    private String maxExperience;
    private String minSalary;
    private String maxSalary;
    private String location;
    private String status;
    private String createdAt;
}
