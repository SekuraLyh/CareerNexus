package com.cn.service.impl;

import com.cn.VO.DashedBorderVO;
import com.cn.VO.SystemOverviewVO;
import com.cn.VO.UserVO;
import com.cn.entity.IndustryReport;
import com.cn.entity.UserAccount;
import com.cn.exception.BusinessException;
import com.cn.mapper.AdminMapper;
import com.cn.result.PageResult;
import com.cn.service.AdminService;
import com.cn.service.ReportService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.cn.constant.MessageConstant.ACCOUNT_NOT_FOUND;
import static com.cn.constant.UserStatusConstant.ACTIVE;
import static com.cn.constant.UserStatusConstant.INACTIVE;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private ReportService reportService;

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

    /**
     * 获取系统概览
     *
     * @return {@link SystemOverviewVO}
     */
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
        List<UserAccount> users = adminMapper.selectUsersByConditions(userType, status, keyword);

        // 3. 封装为 PageInfo（包含总数、总页数等信息）
        PageInfo<UserAccount> pageInfo = new PageInfo<>(users);

        // 4. 使用 PageUtils 工具类转换（Entity -> VO）
        PageResult<UserVO> result = PageUtils.toPageResult(pageInfo, this::convertToUserVO);

        log.info("查询完成: total={}, totalPages={}", result.getTotal(), result.getTotalPages());

        return result;
    }

    /**
     * 获取用户详情
     *
     * @param userId 用户ID
     * @return 用户VO
     */
    @Override
    public UserVO getUserDetail(Long userId) {
        log.info("获取用户详情: userId={}", userId);

        UserAccount user = adminMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException(404, ACCOUNT_NOT_FOUND);
        }

        return convertToUserVO(user);
    }

    @Override
    public PageResult<IndustryReport> listAllReports(Integer page, Integer size, String industry) {
        return reportService.listAllReports(page, size, industry);
    }

    @Override
    public void deleteReport(Long reportId) {
        reportService.deleteReport(reportId);
    }

    /**
     * 更新用户状态
     *
     * @param userId 用户ID
     * @param status 状态（ACTIVE / INACTIVE）
     */
    @Override
    public void updateUserStatus(Long userId, String status) {
        log.info("更新用户状态: userId={}, status={}", userId, status);

        // 1. 验证用户是否存在
        UserAccount user = adminMapper.selectUserById(userId);
        if (user == null) {
            throw new BusinessException(404, ACCOUNT_NOT_FOUND);
        }

        // 2. 验证状态值是否合法
        Set<String> validStatuses = new HashSet<>(Arrays.asList(ACTIVE, INACTIVE));
        if (!validStatuses.contains(status)) {
            throw new BusinessException(400, "无效的状态值，只支持: ACTIVE, INACTIVE");
        }

        // 3. 构建更新对象（只设置需要更新的字段）
        UserAccount updateUser = new UserAccount();
        updateUser.setId(userId);
        updateUser.setStatus(status);

        // 4. 执行动态更新（只更新 status 字段）
        adminMapper.updateUser(updateUser);

        log.info("用户状态更新成功: userId={}, oldStatus={}, newStatus={}",
                userId, user.getStatus(), status);
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
