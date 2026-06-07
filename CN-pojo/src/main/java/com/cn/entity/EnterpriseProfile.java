package com.cn.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnterpriseProfile {
    private Long id;              // 数据库主键

    private Long userId;          // 外键关联用户表

    private String companyName;   // 公司名称
    private String industry;      // 所属行业
    private String companySize;   // 公司规模
    private String companyDescription;  // 公司简介
    private String contactPerson; // 联系人
    private String contactPhone;  // 联系电话
    private String address;       // 公司地址
    private String website;       // 公司官网

    private LocalDateTime createdAt;  // 创建时间（审计字段）
    private LocalDateTime updatedAt;  // 更新时间（审计字段）
}
