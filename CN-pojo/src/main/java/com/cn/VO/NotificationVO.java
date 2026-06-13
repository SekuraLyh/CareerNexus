package com.cn.VO;

import lombok.Data;

@Data
public class NotificationVO {
    private Long id;
    private Long userId;
    private String type;
    private Long relatedId;
    private String message;
    private String targetUrl;
    private Boolean isRead;
    private String createdAt;
}
