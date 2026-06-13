package com.cn.service.impl;

import com.cn.VO.UserVO;
import com.cn.context.BaseContext;
import com.cn.entity.Notification;
import com.cn.entity.UserSubscription;
import com.cn.exception.BusinessException;
import com.cn.mapper.NotificationsMapper;
import com.cn.mapper.UserSubscriptionMapper;
import com.cn.result.PageResult;
import com.cn.service.UserSubscriptionService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    @Autowired
    private UserSubscriptionMapper userSubscriptionMapper;

    @Autowired
    private NotificationsMapper notificationsMapper;

    @Override
    @Transactional
    public void followUser(Long followeeId) {
        Long currentId = BaseContext.getCurrentId();
        if (currentId.equals(followeeId)) {
            throw new BusinessException(400, "不能关注自己");
        }

        Integer count = userSubscriptionMapper.countByFollowerAndFollowee(currentId, followeeId);
        if (count != null && count > 0) {
            throw new BusinessException(400, "已关注该用户");
        }

        UserSubscription subscription = new UserSubscription();
        subscription.setFollowerId(currentId);
        subscription.setFolloweeId(followeeId);
        userSubscriptionMapper.insert(subscription);

        // 发送通知给被关注者
        String followerName = getUserName(currentId);
        Notification notification = new Notification();
        notification.setUserId(followeeId);
        notification.setType("USER_FOLLOWED");
        notification.setMessage(followerName + " 关注了你");
        notification.setTargetUrl("/user/" + currentId);
        notification.setIsRead(false);
        notificationsMapper.insert(notification);

        log.info("关注通知已发送: followeeId={}, followerId={}", followeeId, currentId);
    }

    @Override
    @Transactional
    public void unfollowUser(Long followeeId) {
        Long currentId = BaseContext.getCurrentId();
        userSubscriptionMapper.deleteByFollowerAndFollowee(currentId, followeeId);
    }

    @Override
    public Boolean isFollowing(Long followeeId) {
        Long currentId = BaseContext.getCurrentId();
        return userSubscriptionMapper.isFollowing(currentId, followeeId);
    }

    @Override
    public Integer getFollowerCount(Long userId) {
        return userSubscriptionMapper.countFollowers(userId);
    }

    @Override
    public Integer getFollowingCount(Long userId) {
        if (userId != null) {
            return userSubscriptionMapper.countFollowing(userId);
        }
        Long currentId = BaseContext.getCurrentId();
        return userSubscriptionMapper.countFollowing(currentId);
    }

    @Override
    public void notifyFollowersOfNewPost(Long authorId, Long postId, String postTitle) {
        List<Long> followerIds = userSubscriptionMapper.selectFollowersByFolloweeId(authorId);
        for (Long followerId : followerIds) {
            Notification notification = new Notification();
            notification.setUserId(followerId);
            notification.setType("NEW_POST");
            notification.setMessage("你关注的用户发布了新帖子「" + postTitle + "」");
            notification.setRelatedId(postId);
            notification.setTargetUrl("/forum/posts/" + postId);
            notification.setIsRead(false);
            notificationsMapper.insert(notification);
        }
        if (!followerIds.isEmpty()) {
            log.info("新帖通知已发送给 {} 个关注者: authorId={}, postId={}", followerIds.size(), authorId, postId);
        }
    }

    /**
     * 获取用户名称（根据用户类型）
     */
    private String getUserName(Long userId) {
        // 尝试从 users 表获取 username（这里简化处理，实际可以扩展）
        return "用户" + userId;
    }

    @Override
    public PageResult<UserVO> getFollowerList(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<UserVO> followers = userSubscriptionMapper.selectFollowersList(userId);
        PageInfo<UserVO> pageInfo = new PageInfo<>(followers);
        return PageUtils.toPageResult(pageInfo);
    }

    @Override
    public PageResult<UserVO> getFollowingList(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<UserVO> followees = userSubscriptionMapper.selectFollowingList(userId);
        PageInfo<UserVO> pageInfo = new PageInfo<>(followees);
        return PageUtils.toPageResult(pageInfo);
    }
}
