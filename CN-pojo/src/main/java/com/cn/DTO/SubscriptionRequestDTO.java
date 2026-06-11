package com.cn.DTO;

import lombok.Data;

@Data
public class SubscriptionRequestDTO {
    private String keywords;
    private String major;
    private Integer minSalary;
    private Integer workExperience;
    private String location;
}
