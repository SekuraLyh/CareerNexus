package com.cn.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class JobPosting {
    private Integer id;
    private Integer enterpriseUserId;
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
    private String updatedAt;
}
