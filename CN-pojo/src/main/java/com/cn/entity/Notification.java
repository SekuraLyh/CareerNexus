package com.cn.entity;

import lombok.Data;

@Data
public class Notification {
    private Long id;
    private Long userId;        // 接收通知的用户ID
    private String type;        // 通知类型：FAVORITED/POST_LIKED/USER_FOLLOWED/NEW_POST/POST_COLLECTED等
    private Long relatedId;     // 关联记录ID（收藏/帖子/用户等，可为空）
    private String message;     // 通知消息文本
    private String targetUrl;   // 跳转链接
    private Boolean isRead;     // 是否已读
    private String createdAt;   // 创建时间
}
