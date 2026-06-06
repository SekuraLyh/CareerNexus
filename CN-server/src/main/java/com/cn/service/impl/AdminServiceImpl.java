package com.cn.service.impl;

import com.cn.VO.DashedBorderVO;
import com.cn.mapper.AdminMapper;
import com.cn.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
