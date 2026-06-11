package com.cn.controller.auth;

import com.cn.DTO.SubscriptionRequestDTO;
import com.cn.DTO.ToggleEnabledDTO;
import com.cn.VO.JobPostingVO;
import com.cn.VO.NotificationVO;
import com.cn.VO.SubscriptionsVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.SubscriptionsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@Api(tags = "订阅接口")
@Slf4j
public class SubscriptionsController {

    @Autowired
    private SubscriptionsService subscriptionsService;

    @GetMapping
    @ApiOperation("获取我的订阅列表")
    public Result<List<SubscriptionsVO>> getSubscriptions() {
        log.info("获取当前用户的订阅列表");
        List<SubscriptionsVO> list = subscriptionsService.getSubscriptions();
        return Result.success(list);
    }

    @PostMapping
    @ApiOperation("创建订阅")
    public Result<SubscriptionsVO> createSubscription(@RequestBody SubscriptionRequestDTO dto) {
        log.info("创建订阅: {}", dto);
        return Result.success(subscriptionsService.createSubscription(dto));
    }

    @PutMapping("/{subId}")
    @ApiOperation("更新订阅")
    public Result<Void> updateSubscription(@PathVariable Long subId, @RequestBody SubscriptionRequestDTO dto) {
        log.info("更新订阅 {}: {}", subId, dto);
        subscriptionsService.updateSubscription(subId, dto);
        return Result.success();
    }

    @DeleteMapping("/{subId}")
    @ApiOperation("删除订阅")
    public Result<Void> deleteSubscription(@PathVariable Long subId) {
        log.info("删除订阅 {}", subId);
        subscriptionsService.deleteSubscription(subId);
        return Result.success();
    }

    @PutMapping("/{subId}/toggle")
    @ApiOperation("启用/禁用订阅")
    public Result<Void> toggleSubscription(@PathVariable Long subId, @RequestBody @Valid ToggleEnabledDTO dto) {
        log.info("切换订阅 {} 状态: enabled={}", subId, dto.getEnabled());
        subscriptionsService.toggleSubscription(subId, dto.getEnabled());
        return Result.success();
    }

    @GetMapping("/{subId}/matches")
    @ApiOperation("获取订阅匹配的职位")
    public Result<PageResult<JobPostingVO>> getSubscriptionMatches(
            @PathVariable Long subId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("获取订阅 {} 匹配的职位, page={}, size={}", subId, page, size);
        return Result.success(subscriptionsService.getSubscriptionMatches(subId, page, size));
    }

    @GetMapping("/notifications")
    @ApiOperation("获取推送通知列表")
    public Result<PageResult<NotificationVO>> getNotifications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Boolean read) {
        log.info("获取通知列表, page={}, size={}, read={}", page, size, read);
        return Result.success(subscriptionsService.getNotifications(page, size, read));
    }

    @PutMapping("/notifications/{notificationId}/read")
    @ApiOperation("标记通知为已读")
    public Result<Void> markNotificationRead(@PathVariable Long notificationId) {
        log.info("标记通知 {} 为已读", notificationId);
        subscriptionsService.markNotificationRead(notificationId);
        return Result.success();
    }

    @GetMapping("/notifications/unread-count")
    @ApiOperation("获取未读通知数")
    public Result<Integer> getUnreadCount() {
        log.info("获取未读通知数");
        return Result.success(subscriptionsService.getUnreadCount());
    }
}
