package com.cn.mapper;

import com.cn.VO.NotificationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationsMapper {
    List<NotificationVO> getByUserId(@Param("userId") Long userId, @Param("isRead") Boolean isRead);

    NotificationVO selectById(Long id);

    void markAsRead(Long id);

    Integer countUnread(Long userId);
}
