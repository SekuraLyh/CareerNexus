package com.cn.service.impl;

import com.cn.DTO.EnterpriseProfileDTO;
import com.cn.DTO.FavoriteRequestDTO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.VO.FavoriteVO;
import com.cn.VO.NotificationVO;
import com.cn.context.BaseContext;
import com.cn.entity.Favorite;
import com.cn.entity.ForumPost;
import com.cn.entity.IndustryReport;
import com.cn.entity.JobPosting;
import com.cn.entity.Notification;
import com.cn.exception.BusinessException;
import com.cn.mapper.FavoritesMapper;
import com.cn.mapper.ForumMapper;
import com.cn.mapper.JobsMapper;
import com.cn.mapper.NotificationsMapper;
import com.cn.mapper.ProfileMapper;
import com.cn.mapper.ReportMapper;
import com.cn.result.PageResult;
import com.cn.service.FavoriteService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.cn.constant.MessageConstant.*;

@Service
@Slf4j
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoritesMapper favoritesMapper;

    @Autowired
    private NotificationsMapper notificationsMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private JobsMapper jobsMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private ForumMapper forumMapper;

    @Override
    public FavoriteVO addFavorite(FavoriteRequestDTO dto) {
        Long currentId = BaseContext.getCurrentId();

        // 检查是否已收藏
        Integer count = favoritesMapper.countByTarget(currentId, dto.getTargetType(), dto.getTargetId());
        if (count != null && count > 0) {
            throw new BusinessException(400, ALREADY_FAVORITED);
        }

        // 插入收藏
        Favorite favorite = new Favorite();
        favorite.setSubscriberId(currentId);
        favorite.setTargetType(dto.getTargetType());
        favorite.setTargetId(dto.getTargetId());
        favoritesMapper.insert(favorite);

        // 发送通知给被订阅方
        sendNotification(favorite);

        // 返回完整的 FavoriteVO
        List<FavoriteVO> list = favoritesMapper.getBySubscriberId(currentId, dto.getTargetType());
        return list.stream()
                .filter(vo -> vo.getTargetId().equals(dto.getTargetId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据收藏记录发送通知给被订阅方
     */
    private void sendNotification(Favorite favorite) {
        Long currentId = favorite.getSubscriberId();
        String targetType = favorite.getTargetType();
        Long targetId = favorite.getTargetId();

        Notification notification = new Notification();
        notification.setRelatedId(favorite.getId());

        switch (targetType) {
            case "JOB": {
                // 求职者收藏岗位 → 通知发布该岗位的企业
                JobPosting job = jobsMapper.selectById(targetId.intValue());
                if (job == null) return;

                JobSeekerProfileDTO subscriber = profileMapper.getJobSeekerProfileById(currentId);
                String subscriberName = subscriber != null && subscriber.getRealName() != null
                        ? subscriber.getRealName() : "一位求职者";

                notification.setUserId(Long.valueOf(job.getEnterpriseUserId()));
                notification.setType("FAVORITED");
                notification.setMessage(subscriberName + " 收藏了你发布的岗位「" + job.getTitle() + "」");
                notification.setTargetUrl("/jobs/" + targetId);
                break;
            }
            case "JOB_SEEKER": {
                // 企业收藏求职者 → 通知该求职者
                EnterpriseProfileDTO subscriber = profileMapper.getEnterpriseProfileById(currentId);
                String subscriberName = subscriber != null && subscriber.getCompanyName() != null
                        ? subscriber.getCompanyName() : "一家企业";

                notification.setUserId(targetId);
                notification.setType("FAVORITED");
                notification.setMessage(subscriberName + " 关注了你的简历");
                notification.setTargetUrl("/search/job-seekers/" + targetId);
                break;
            }
            case "ENTERPRISE": {
                // 求职者收藏企业 → 通知该企业
                JobSeekerProfileDTO subscriber = profileMapper.getJobSeekerProfileById(currentId);
                String subscriberName = subscriber != null && subscriber.getRealName() != null
                        ? subscriber.getRealName() : "一位求职者";

                notification.setUserId(targetId);
                notification.setType("FAVORITED");
                notification.setMessage(subscriberName + " 关注了你的企业");
                notification.setTargetUrl("/enterprise/" + targetId);
                break;
            }
            case "POST": {
                // 收藏帖子 → 通知帖子作者
                ForumPost post = forumMapper.selectPostEntityById(targetId);
                if (post == null) return;

                String subscriberName = getSubscriberName(currentId);

                notification.setUserId(post.getUserId());
                notification.setType("POST_COLLECTED");
                notification.setMessage(subscriberName + " 收藏了你的帖子「" + post.getTitle() + "」");
                notification.setTargetUrl("/forum/posts/" + targetId);
                break;
            }
            case "INDUSTRY_REPORT": {
                // 收藏行业报告 → 仅记录，无特定被通知方（系统生成内容）
                // 可以通知管理员或跳过
                return;
            }
            default:
                return;
        }

        notificationsMapper.insert(notification);
        log.info("通知已发送: favoriteId={}, targetType={}, notifyUserId={}",
                favorite.getId(), targetType, notification.getUserId());
    }

    /**
     * 获取收藏者名称
     */
    private String getSubscriberName(Long userId) {
        JobSeekerProfileDTO seeker = profileMapper.getJobSeekerProfileById(userId);
        if (seeker != null && seeker.getRealName() != null) {
            return seeker.getRealName();
        }

        EnterpriseProfileDTO enterprise = profileMapper.getEnterpriseProfileById(userId);
        if (enterprise != null && enterprise.getCompanyName() != null) {
            return enterprise.getCompanyName();
        }

        return "用户";
    }

    @Override
    public void removeFavorite(Long favoriteId) {
        Long currentId = BaseContext.getCurrentId();
        Favorite existing = favoritesMapper.selectById(favoriteId);
        if (existing == null) {
            throw new BusinessException(404, FAVORITE_NOT_FOUND);
        }
        if (!existing.getSubscriberId().equals(currentId)) {
            throw new BusinessException(403, FAVORITE_NOT_OWNER);
        }
        favoritesMapper.deleteById(favoriteId);
    }

    @Override
    public List<FavoriteVO> getMyFavorites(String targetType) {
        Long currentId = BaseContext.getCurrentId();
        return favoritesMapper.getBySubscriberId(currentId, targetType);
    }

    @Override
    public Boolean isFavorited(String targetType, Long targetId) {
        Long currentId = BaseContext.getCurrentId();
        Integer count = favoritesMapper.countByTarget(currentId, targetType, targetId);
        return count != null && count > 0;
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
}
