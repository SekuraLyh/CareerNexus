package com.cn.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    
    /**
     * 统计职位数量
     */
    @Select("SELECT COUNT(*) FROM job_postings WHERE status = 'OPEN'")
    Integer countJobs();
    
    /**
     * 统计行业报告数量
     */
    @Select("SELECT COUNT(*) FROM industry_reports")
    Integer countReports();
    
    /**
     * 统计论坛帖子数量（排除已删除的）
     */
    @Select("SELECT COUNT(*) FROM forum_posts WHERE is_deleted = 0")
    Integer countPosts();
    
    /**
     * 统计用户数量（排除已注销的）
     */
    @Select("SELECT COUNT(*) FROM users WHERE status = 'ACTIVE'")
    Integer countUsers();
}
