package com.cn.service.impl;

import com.cn.DTO.JobPostDTO;
import com.cn.DTO.JobQueryDTO;
import com.cn.VO.JobPostingVO;
import com.cn.context.BaseContext;
import com.cn.entity.JobPosting;
import com.cn.entity.UserAccount;
import com.cn.exception.BusinessException;
import com.cn.mapper.JobsMapper;
import com.cn.result.PageResult;
import com.cn.service.JobsService;
import com.cn.service.UserService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.cn.constant.JobsStatusConstant.CLOSED;
import static com.cn.constant.JobsStatusConstant.OPEN;
import static com.cn.constant.UserTypeConstant.ENTERPRISE;

@Service
@Slf4j
public class JobsServiceImpl implements JobsService {
    @Autowired
    private JobsMapper jobsMapper;
    @Autowired
    private UserService userService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public PageResult<JobPostingVO> getJobs(JobQueryDTO jobQuery) {
        PageHelper.startPage(jobQuery.getPage(), jobQuery.getSize());

        List<JobPosting> jobs = jobsMapper.selectJosByConditions(
                jobQuery.getMajor(),
                jobQuery.getKeyword(),
                jobQuery.getMinSalary(),
                jobQuery.getLocation());

        PageInfo<JobPosting> pageInfo = new PageInfo<>(jobs);
        return PageUtils.toPageResult(pageInfo, this::convertToJobPostingVO);
    }

    @Override
    public JobPostingVO createJob(JobPostDTO jobPostDTO) {
        Long userId = BaseContext.getCurrentId();

        // 1. 校验仅企业用户可发布职位
        UserAccount user = userService.getUserById(userId);
        if (!ENTERPRISE.equals(user.getUserType())) {
            throw new BusinessException(403, "仅企业用户可发布职位");
        }

        // 2. 获取企业名称
        String enterpriseName = jobsMapper.getCompanyNameByUserId(userId);
        if (enterpriseName == null || enterpriseName.isEmpty()) {
            throw new BusinessException(400, "请先完善企业档案信息");
        }

        // 3. 构建 JobPosting 实体
        JobPosting job = new JobPosting();
        job.setTitle(jobPostDTO.getTitle());
        job.setDescription(jobPostDTO.getDescription());
        job.setRequiredMajor(jobPostDTO.getRequiredMajor());
        job.setMinExperience(jobPostDTO.getMinExperience() != null ? String.valueOf(jobPostDTO.getMinExperience()) : null);
        job.setMaxExperience(jobPostDTO.getMaxExperience() != null ? String.valueOf(jobPostDTO.getMaxExperience()) : null);
        job.setMinSalary(jobPostDTO.getMinSalary() != null ? String.valueOf(jobPostDTO.getMinSalary()) : null);
        job.setMaxSalary(jobPostDTO.getMaxSalary() != null ? String.valueOf(jobPostDTO.getMaxSalary()) : null);
        job.setLocation(jobPostDTO.getLocation());
        job.setEnterpriseUserId(userId.intValue());
        job.setEnterpriseName(enterpriseName);
        job.setStatus(OPEN);

        String now = LocalDateTime.now().format(FORMATTER);
        job.setCreatedAt(now);
        job.setUpdatedAt(now);

        // 4. 插入数据库
        jobsMapper.insertJob(job);
        log.info("职位发布成功, jobId: {}, title: {}, enterpriseName: {}", job.getId(), job.getTitle(), enterpriseName);

        // 5. 返回 VO
        return convertToJobPostingVO(job);
    }

    @Override
    public JobPostingVO getJobDetail(Integer jobId) {
        JobPosting job = jobsMapper.selectById(jobId);
        return convertToJobPostingVO(job);
    }

    @Override
    public JobPostingVO updateJob(Integer jobId, JobPostDTO jobPostDTO) {

        jobsMapper.updateJob(
                jobId,
                jobPostDTO.getTitle(),
                jobPostDTO.getDescription(),
                jobPostDTO.getRequiredMajor(),
                jobPostDTO.getMinExperience(),
                jobPostDTO.getMaxExperience(),
                jobPostDTO.getMinSalary(),
                jobPostDTO.getMaxSalary(),
                jobPostDTO.getLocation()
        );

        return convertToJobPostingVO(jobsMapper.selectById(jobId));
    }

    @Override
    public PageResult<JobPostingVO> getMyJobs(Integer page, Integer size, String status) {
        Long userId = BaseContext.getCurrentId();

        // 1. 校验仅企业用户可查询自己发布的职位
        UserAccount user = userService.getUserById(userId);
        if (!ENTERPRISE.equals(user.getUserType())) {
            throw new BusinessException(403, "仅企业用户可查看已发布的职位");
        }

        // 2. 分页查询
        PageHelper.startPage(page, size);
        List<JobPosting> jobs = jobsMapper.searchMyJob(userId, status);
        PageInfo<JobPosting> pageInfo = new PageInfo<>(jobs);
        return PageUtils.toPageResult(pageInfo, this::convertToJobPostingVO);
    }

    @Override
    public void deleteJob(Integer jobId) {
        jobsMapper.deleteJob(jobId);
    }

    @Override
    public void updateJobStatus(Integer jobId, String status) {
        Long userId = BaseContext.getCurrentId();

        // 1. 校验职位是否存在且属于当前用户
        JobPosting job = jobsMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(404, "职位不存在");
        }
        if (!userId.equals(Long.valueOf(job.getEnterpriseUserId()))) {
            throw new BusinessException(403, "无权操作该职位");
        }

        // 2. 校验状态值合法
        if (!OPEN.equals(status) && !CLOSED.equals(status)) {
            throw new BusinessException(400, "无效的状态值，仅支持 OPEN 或 CLOSED");
        }

        // 3. 更新状态
        jobsMapper.updateJobStatus(jobId, status);
        log.info("职位状态已更新, jobId: {}, status: {}, userId: {}", jobId, status, userId);
    }

    private JobPostingVO convertToJobPostingVO(JobPosting job) {
        JobPostingVO vo = new JobPostingVO();
        BeanUtils.copyProperties(job, vo);
        return vo;
    }
}
