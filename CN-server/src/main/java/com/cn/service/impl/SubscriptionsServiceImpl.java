package com.cn.service.impl;

import com.cn.DTO.SubscriptionRequestDTO;
import com.cn.VO.JobPostingVO;
import com.cn.VO.NotificationVO;
import com.cn.VO.SubscriptionsVO;
import com.cn.context.BaseContext;
import com.cn.entity.JobPosting;
import com.cn.entity.Subscriptions;
import com.cn.exception.BusinessException;
import com.cn.mapper.NotificationsMapper;
import com.cn.mapper.SubscriptionsMapper;
import com.cn.result.PageResult;
import com.cn.service.SubscriptionsService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.cn.constant.MessageConstant.NOTIFICATION_NOT_FOUND;
import static com.cn.constant.MessageConstant.SUBSCRIPTION_NOT_FOUND;
import static com.cn.constant.MessageConstant.SUBSCRIPTION_NOT_OWNER;

@Service
@Slf4j
public class SubscriptionsServiceImpl implements SubscriptionsService {

    @Autowired
    private SubscriptionsMapper subscriptionsMapper;

    @Autowired
    private NotificationsMapper notificationsMapper;

    @Override
    public List<SubscriptionsVO> getSubscriptions() {
        Long currentId = BaseContext.getCurrentId();
        return subscriptionsMapper.getSubscriptionsByUserId(currentId);
    }

    @Override
    public SubscriptionsVO createSubscription(SubscriptionRequestDTO dto) {
        Long currentId = BaseContext.getCurrentId();
        Subscriptions subscription = new Subscriptions();
        BeanUtils.copyProperties(dto, subscription);
        subscription.setUserId(currentId);
        subscription.setEnabled(true);
        subscriptionsMapper.insert(subscription);

        // 回查返回完整VO
        SubscriptionsVO vo = new SubscriptionsVO();
        vo.setId(subscription.getId());
        vo.setUserId(currentId);
        vo.setKeywords(dto.getKeywords());
        vo.setMajor(dto.getMajor());
        vo.setMinSalary(dto.getMinSalary());
        vo.setWorkExperience(dto.getWorkExperience());
        vo.setLocation(dto.getLocation());
        vo.setEnabled(true);
        return vo;
    }

    @Override
    public void updateSubscription(Long subId, SubscriptionRequestDTO dto) {
        Long currentId = BaseContext.getCurrentId();
        Subscriptions existing = subscriptionsMapper.selectById(subId);
        if (existing == null) {
            throw new BusinessException(404, SUBSCRIPTION_NOT_FOUND);
        }
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, SUBSCRIPTION_NOT_OWNER);
        }
        Subscriptions update = new Subscriptions();
        BeanUtils.copyProperties(dto, update);
        update.setId(subId);
        subscriptionsMapper.update(update);
    }

    @Override
    public void deleteSubscription(Long subId) {
        Long currentId = BaseContext.getCurrentId();
        Subscriptions existing = subscriptionsMapper.selectById(subId);
        if (existing == null) {
            throw new BusinessException(404, SUBSCRIPTION_NOT_FOUND);
        }
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, SUBSCRIPTION_NOT_OWNER);
        }
        subscriptionsMapper.deleteById(subId);
    }

    @Override
    public void toggleSubscription(Long subId, Boolean enabled) {
        Long currentId = BaseContext.getCurrentId();
        Subscriptions existing = subscriptionsMapper.selectById(subId);
        if (existing == null) {
            throw new BusinessException(404, SUBSCRIPTION_NOT_FOUND);
        }
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, SUBSCRIPTION_NOT_OWNER);
        }
        subscriptionsMapper.updateEnabled(subId, enabled);
    }

    @Override
    public PageResult<JobPostingVO> getSubscriptionMatches(Long subId, Integer page, Integer size) {
        Long currentId = BaseContext.getCurrentId();
        Subscriptions existing = subscriptionsMapper.selectById(subId);
        if (existing == null) {
            throw new BusinessException(404, SUBSCRIPTION_NOT_FOUND);
        }
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, SUBSCRIPTION_NOT_OWNER);
        }

        // 将 keywords 逗号分隔为列表
        List<String> keywordList = new ArrayList<>();
        if (existing.getKeywords() != null && !existing.getKeywords().trim().isEmpty()) {
            keywordList = Arrays.stream(existing.getKeywords().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        PageHelper.startPage(page, size);
        List<JobPosting> jobs = subscriptionsMapper.matchJobs(
                keywordList, existing.getMajor(), existing.getMinSalary(),
                existing.getWorkExperience(), existing.getLocation()
        );
        PageInfo<JobPosting> pageInfo = new PageInfo<>(jobs);

        // Entity -> VO
        return PageUtils.toPageResult(pageInfo, this::toJobPostingVO);
    }

    @Override
    public PageResult<NotificationVO> getNotifications(Integer page, Integer size, Boolean read) {
        Long currentId = BaseContext.getCurrentId();
        PageHelper.startPage(page, size);
        List<NotificationVO> list = notificationsMapper.getByUserId(currentId, read);
        PageInfo<NotificationVO> pageInfo = new PageInfo<>(list);
        return PageUtils.toPageResult(pageInfo);
    }

    @Override
    public void markNotificationRead(Long notificationId) {
        Long currentId = BaseContext.getCurrentId();
        NotificationVO notification = notificationsMapper.selectById(notificationId);
        if (notification == null) {
            throw new BusinessException(404, NOTIFICATION_NOT_FOUND);
        }
        notificationsMapper.markAsRead(notificationId);
    }

    @Override
    public Integer getUnreadCount() {
        Long currentId = BaseContext.getCurrentId();
        return notificationsMapper.countUnread(currentId);
    }

    private JobPostingVO toJobPostingVO(JobPosting entity) {
        JobPostingVO vo = new JobPostingVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
