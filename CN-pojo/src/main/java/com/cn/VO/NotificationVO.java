package com.cn.VO;

import lombok.Data;

@Data
public class NotificationVO {
    private Long id;
    private Long subscriptionId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String matchedAt;
    private Boolean isRead;
}
