package com.cn.service;

import com.cn.DTO.FavoriteRequestDTO;
import com.cn.VO.FavoriteVO;
import com.cn.VO.NotificationVO;
import com.cn.result.PageResult;

import java.util.List;

public interface FavoriteService {

    /** 添加收藏，并发送通知给被订阅方 */
    FavoriteVO addFavorite(FavoriteRequestDTO dto);

    /** 取消收藏（按收藏ID） */
    void removeFavorite(Long favoriteId);

    /** 获取我的收藏列表（可选 targetType 筛选） */
    List<FavoriteVO> getMyFavorites(String targetType);

    /** 检查是否已收藏 */
    Boolean isFavorited(String targetType, Long targetId);

    /** 获取通知列表 */
    PageResult<NotificationVO> getNotifications(Integer page, Integer size, Boolean read);

    /** 标记通知已读 */
    void markNotificationRead(Long notificationId);

    /** 未读数量 */
    Integer getUnreadCount();
}
