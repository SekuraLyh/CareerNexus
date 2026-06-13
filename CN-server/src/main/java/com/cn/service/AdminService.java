package com.cn.service;

import com.cn.VO.DashedBorderVO;
import com.cn.VO.SystemOverviewVO;
import com.cn.VO.UserVO;
import com.cn.entity.IndustryReport;
import com.cn.VO.PostVO;
import com.cn.result.PageResult;

public interface AdminService {
    DashedBorderVO getDashedBorder();

    SystemOverviewVO getSystemOverview();

    /**
     * 分页查询用户列表
     *
     * @param page     页码（从1开始）
     * @param size     每页大小
     * @param userType 用户类型（可选）
     * @param status   账号状态（可选）
     * @param keyword  搜索关键词（可选，支持用户名/邮箱/手机号）
     * @return 分页结果
     */
    PageResult<UserVO> getUsers(Integer page, Integer size, String userType, String status, String keyword);

    /**
     * 获取用户详情
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserDetail(Long userId);

    void updateUserStatus(Long userId, String status);

    /** 分页获取所有行业报告 */
    PageResult<IndustryReport> listAllReports(Integer page, Integer size, String industry);

    /** 删除行业报告 */
    void deleteReport(Long reportId);

    /** 分页获取所有帖子 */
    PageResult<PostVO> listAllPosts(Integer page, Integer size, String keyword);

    /** 管理员删除帖子 */
    void adminDeletePost(Long postId);

    /** 管理员删除评论 */
    void adminDeleteComment(Long commentId);
}
