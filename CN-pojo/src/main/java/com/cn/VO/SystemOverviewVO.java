package com.cn.VO;

import lombok.Data;

@Data
public class SystemOverviewVO {
    /**
     * 开放中的职位数
     */
    private Integer jobsOpen;
    /**
     * 职位关闭
     */
    private Integer jobsClosed;
    /**
     * 生成的报告
     */
    private Integer reportsGenerated;
    /**
     * 本月帖子
     */
    private Integer postsThisMonth;
}
