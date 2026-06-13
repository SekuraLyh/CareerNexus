package com.cn.mapper;

import com.cn.VO.NotificationVO;
import com.cn.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationsMapper {

    /** 新增通知 */
    void insert(Notification notification);

    /** 按用户查询通知列表（可选已读筛选） */
    List<NotificationVO> getByUserId(@Param("userId") Long userId,
                                      @Param("isRead") Boolean isRead);

    /** 按ID查询 */
    NotificationVO selectById(@Param("id") Long id);

    /** 标记为已读 */
    void markAsRead(@Param("id") Long id);

    /** 未读数量 */
    Integer countUnread(@Param("userId") Long userId);
}
