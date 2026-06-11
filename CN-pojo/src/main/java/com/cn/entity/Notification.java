package com.cn.entity;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private Long subscriptionId;
    private Long userId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String matchedAt;
    private Boolean isRead;
    private String createdAt;
}
