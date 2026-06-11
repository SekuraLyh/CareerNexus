package com.cn.service;

import com.cn.DTO.SubscriptionRequestDTO;
import com.cn.VO.NotificationVO;
import com.cn.VO.SubscriptionsVO;
import com.cn.result.PageResult;
import com.cn.VO.JobPostingVO;

import java.util.List;

public interface SubscriptionsService {
    List<SubscriptionsVO> getSubscriptions();

    SubscriptionsVO createSubscription(SubscriptionRequestDTO dto);

    void updateSubscription(Long subId, SubscriptionRequestDTO dto);

    void deleteSubscription(Long subId);

    void toggleSubscription(Long subId, Boolean enabled);

    PageResult<JobPostingVO> getSubscriptionMatches(Long subId, Integer page, Integer size);

    PageResult<NotificationVO> getNotifications(Integer page, Integer size, Boolean read);

    void markNotificationRead(Long notificationId);

    Integer getUnreadCount();
}
