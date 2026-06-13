package com.cn.controller.auth;

import com.cn.VO.UserVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.UserSubscriptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions/users")
@Slf4j
public class UserSubscriptionController {

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    /** POST /api/subscriptions/users/{followeeId} — 关注用户 */
    @PostMapping("/{followeeId}")
    public Result<Void> follow(@PathVariable Long followeeId) {
        userSubscriptionService.followUser(followeeId);
        return Result.success();
    }

    /** DELETE /api/subscriptions/users/{followeeId} — 取消关注 */
    @DeleteMapping("/{followeeId}")
    public Result<Void> unfollow(@PathVariable Long followeeId) {
        userSubscriptionService.unfollowUser(followeeId);
        return Result.success();
    }

    /** GET /api/subscriptions/users/{followeeId}/check — 检查是否关注 */
    @GetMapping("/{followeeId}/check")
    public Result<Boolean> isFollowing(@PathVariable Long followeeId) {
        return Result.success(userSubscriptionService.isFollowing(followeeId));
    }

    /** GET /api/subscriptions/users/{userId}/followers — 粉丝数 */
    @GetMapping("/{userId}/followers")
    public Result<Integer> getFollowerCount(@PathVariable Long userId) {
        return Result.success(userSubscriptionService.getFollowerCount(userId));
    }

    /** GET /api/subscriptions/users/following — 当前用户的关注数（可选 userId 查询指定用户） */
    @GetMapping("/following")
    public Result<Integer> getFollowingCount(@RequestParam(required = false) Long userId) {
        return Result.success(userSubscriptionService.getFollowingCount(userId));
    }

    /** GET /api/subscriptions/users/{userId}/followers/list — 粉丝列表 */
    @GetMapping("/{userId}/followers/list")
    public Result<PageResult<UserVO>> getFollowerList(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(userSubscriptionService.getFollowerList(userId, page, size));
    }

    /** GET /api/subscriptions/users/{userId}/following/list — 关注列表 */
    @GetMapping("/{userId}/following/list")
    public Result<PageResult<UserVO>> getFollowingList(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return Result.success(userSubscriptionService.getFollowingList(userId, page, size));
    }
}
