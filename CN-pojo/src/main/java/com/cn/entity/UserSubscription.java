package com.cn.entity;

import lombok.Data;

@Data
public class UserSubscription {
    private Long id;
    private Long followerId;     // 关注者
    private Long followeeId;     // 被关注者
    private String createdAt;
}
