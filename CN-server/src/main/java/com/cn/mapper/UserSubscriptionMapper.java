package com.cn.mapper;

import com.cn.VO.UserVO;
import com.cn.entity.UserSubscription;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserSubscriptionMapper {

    /** 添加关注 */
    void insert(UserSubscription subscription);

    /** 取消关注 */
    void deleteByFollowerAndFollowee(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    /** 检查是否已关注 */
    Integer countByFollowerAndFollowee(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    /** 查询关注者的用户 ID 列表 */
    List<Long> selectFollowersByFolloweeId(@Param("followeeId") Long followeeId);

    /** 检查关注状态 */
    Boolean isFollowing(@Param("followerId") Long followerId, @Param("followeeId") Long followeeId);

    /** 获取粉丝数 */
    Integer countFollowers(@Param("followeeId") Long followeeId);

    /** 获取关注数 */
    Integer countFollowing(@Param("followerId") Long followerId);

    /** 分页获取粉丝列表（含用户基本信息） */
    List<UserVO> selectFollowersList(@Param("followeeId") Long followeeId);

    /** 分页获取关注列表（含用户基本信息） */
    List<UserVO> selectFollowingList(@Param("followerId") Long followerId);
}
