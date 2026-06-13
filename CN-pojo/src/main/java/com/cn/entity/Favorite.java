package com.cn.entity;

import lombok.Data;

@Data
public class Favorite {
    private Long id;
    private Long subscriberId;
    private String targetType;   // JOB_SEEKER, ENTERPRISE, JOB
    private Long targetId;
    private String createdAt;
}
