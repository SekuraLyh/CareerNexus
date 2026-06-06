package com.cn.service.impl;

import com.cn.VO.DashedBorderVO;
import com.cn.VO.SystemOverviewVO;
import com.cn.VO.UserVO;
import com.cn.entity.UserAccount;
import com.cn.mapper.AdminMapper;
import com.cn.result.PageResult;
import com.cn.service.AdminService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminMapper adminMapper;
    
    @Override
    public DashedBorderVO getDashedBorder() {
        log.info("获取管理员仪表盘统计数据");
        
        DashedBorderVO vo = new DashedBorderVO();
        vo.setJobCount(adminMapper.countJobs());
        vo.setReportCount(adminMapper.countReports());
        vo.setPostCount(adminMapper.countPosts());
        vo.setUserCount(adminMapper.countUsers());
        
        log.info("统计数据 - 职位: {}, 行业报告: {}, 论坛帖子: {}, 用户: {}", 
                vo.getJobCount(), vo.getReportCount(),
                vo.getPostCount(), vo.getUserCount());
        
        return vo;
    }

    @Override
    public SystemOverviewVO getSystemOverview() {
        log.info("获取系统概览");

        SystemOverviewVO vo = new SystemOverviewVO();
        vo.setJobsOpen(adminMapper.countJobsOpen());
        vo.setJobsClosed(adminMapper.countJobsClosed());
        vo.setReportsGenerated(adminMapper.countReportsGenerated());
        vo.setPostsThisMonth(adminMapper.countPostsThisMonth());

        return vo;
    }

    /**
     * 分页查询用户列表
     * 使用 PageHelper 实现分页，通过 PageUtils 工具类封装结果
     */
    @Override
    public PageResult<UserVO> getUsers(Integer page, Integer size, String userType, String status, String keyword) {
        log.info("分页查询用户列表: page={}, size={}, userType={}, status={}, keyword={}",
                page, size, userType, status, keyword);

        // 1. 设置分页参数（必须在查询之前调用）
        PageHelper.startPage(page, size);

        // 2. 执行查询（优先使用组合条件查询，支持灵活筛选）
        java.util.List<UserAccount> users = adminMapper.selectUsersByConditions(userType, status, keyword);

        // 3. 封装为 PageInfo（包含总数、总页数等信息）
        PageInfo<UserAccount> pageInfo = new PageInfo<>(users);

        // 4. 使用 PageUtils 工具类转换（Entity -> VO）
        PageResult<UserVO> result = PageUtils.toPageResult(pageInfo, this::convertToUserVO);

        log.info("查询完成: total={}, totalPages={}", result.getTotal(), result.getTotalPages());

        return result;
    }

    /**
     * Entity 转 VO（提取为独立方法，便于复用和维护）
     */
    private UserVO convertToUserVO(UserAccount user) {
        return UserVO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .userType(user.getUserType())
                .email(user.getEmail())
                .phone(user.getPhone())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
