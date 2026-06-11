package com.cn.entity;

import lombok.Data;

@Data
public class Subscriptions {
    private Long id;
    private Long userId;
    private String keywords;
    private String major;
    private Integer minSalary;
    private Integer workExperience;
    private String location;
    private Boolean enabled;
    private String createdAt;
    private String updatedAt;
}
