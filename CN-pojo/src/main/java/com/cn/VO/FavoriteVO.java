package com.cn.VO;

import lombok.Data;

@Data
public class FavoriteVO {
    private Long id;
    private Long subscriberId;
    private String targetType;
    private Long targetId;
    private String createdAt;

    // 目标摘要（根据 targetType 填充不同字段）
    private String targetName;    // 求职者姓名 / 企业名称 / 职位标题
    private String targetDesc;    // 专业/行业/公司名
    private String targetExtra;   // 额外信息（期望薪资/地点等）
}
