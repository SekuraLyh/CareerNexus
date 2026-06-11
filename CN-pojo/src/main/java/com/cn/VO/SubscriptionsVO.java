package com.cn.VO;

import lombok.Data;

@Data
public class SubscriptionsVO {
    private Long id;
    private Long userId;
    private String keywords;
    private String major;
    private Integer minSalary;
    private Integer workExperience;
    private String location;
    private Boolean enabled;
    private String createdAt;
}
