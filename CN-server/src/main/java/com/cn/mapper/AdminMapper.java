package com.cn.mapper;

import com.cn.entity.UserAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {
    
    /**
     * 统计职位数量
     */
    Integer countJobs();
    
    /**
     * 统计行业报告数量
     */
    Integer countReports();
    
    /**
     * 统计论坛帖子数量（排除已删除的）
     */
    Integer countPosts();
    
    /**
     * 统计用户数量（排除已注销的）
     */
    Integer countUsers();

    /**
     * 获取职位数量（已发布的）
     */
    Integer countJobsOpen();

    /**
     * 获取职位数量（已关闭的）
     */
    Integer countJobsClosed();


    /**
     * 获取本月生成报告数
     */
    Integer countReportsGenerated();

    /**
     * 获取本月发帖数
     */
    Integer countPostsThisMonth();

    // ==================== 用户管理相关方法 ====================

    /**
     * 查询所有用户列表（按创建时间降序）
     * PageHelper 会自动拦截并添加 LIMIT
     */
    List<UserAccount> selectAllUsers();

    /**
     * 根据用户类型查询用户列表
     */
    List<UserAccount> selectUsersByType(@Param("userType") String userType);

    /**
     * 根据状态查询用户列表
     */
    List<UserAccount> selectUsersByStatus(@Param("status") String status);

    /**
     * 关键词搜索用户（用户名、邮箱、手机号）
     */
    List<UserAccount> searchUsersByKeyword(@Param("keyword") String keyword);

    /**
     * 组合条件查询用户
     * 使用 MyBatis 动态 SQL，根据参数灵活构建查询条件
     */
    List<UserAccount> selectUsersByConditions(
            @Param("userType") String userType,
            @Param("status") String status,
            @Param("keyword") String keyword
    );
}
