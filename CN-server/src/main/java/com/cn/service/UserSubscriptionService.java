package com.cn.service;

import com.cn.VO.UserVO;
import com.cn.result.PageResult;

public interface UserSubscriptionService {

    /** 关注用户 */
    void followUser(Long followeeId);

    /** 取消关注 */
    void unfollowUser(Long followeeId);

    /** 检查是否关注了指定用户 */
    Boolean isFollowing(Long followeeId);

    /** 获取用户的粉丝数 */
    Integer getFollowerCount(Long userId);

    /** 获取当前用户或指定用户的关注数（userId 为 null 时取当前用户） */
    Integer getFollowingCount(Long userId);

    /** 通知关注者：用户发布了新帖子 */
    void notifyFollowersOfNewPost(Long authorId, Long postId, String postTitle);

    /** 分页获取粉丝列表 */
    PageResult<UserVO> getFollowerList(Long userId, Integer page, Integer size);

    /** 分页获取关注列表 */
    PageResult<UserVO> getFollowingList(Long userId, Integer page, Integer size);
}
