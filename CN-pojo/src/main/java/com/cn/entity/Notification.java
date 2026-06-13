package com.cn.entity;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private Long userId;
    private String type;        // FAVORITED
    private Long relatedId;     // 关联 favorites.id
    private String message;
    private String targetUrl;
    private Boolean isRead;
    private String createdAt;
}
