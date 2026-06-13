package com.cn.controller.auth;

import com.cn.DTO.FavoriteRequestDTO;
import com.cn.VO.FavoriteVO;
import com.cn.VO.NotificationVO;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.FavoriteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/favorites")
@Api(tags = "收藏接口")
@Slf4j
public class FavoriteController {

    @Autowired
    private FavoriteService favoriteService;

    @PostMapping
    @ApiOperation("添加收藏")
    public Result<FavoriteVO> addFavorite(@RequestBody @Valid FavoriteRequestDTO dto) {
        log.info("添加收藏: targetType={}, targetId={}", dto.getTargetType(), dto.getTargetId());
        return Result.success(favoriteService.addFavorite(dto));
    }

    @DeleteMapping("/{favoriteId}")
    @ApiOperation("取消收藏")
    public Result<Void> removeFavorite(@PathVariable Long favoriteId) {
        log.info("取消收藏: id={}", favoriteId);
        favoriteService.removeFavorite(favoriteId);
        return Result.success();
    }

    @GetMapping
    @ApiOperation("获取我的收藏列表")
    public Result<List<FavoriteVO>> getMyFavorites(@RequestParam(required = false) String targetType) {
        log.info("获取收藏列表: targetType={}", targetType);
        return Result.success(favoriteService.getMyFavorites(targetType));
    }

    @GetMapping("/check")
    @ApiOperation("检查是否已收藏")
    public Result<Boolean> checkFavorited(@RequestParam String targetType,
                                          @RequestParam Long targetId) {
        log.info("检查收藏: targetType={}, targetId={}", targetType, targetId);
        return Result.success(favoriteService.isFavorited(targetType, targetId));
    }

    @GetMapping("/notifications")
    @ApiOperation("获取通知列表")
    public Result<PageResult<NotificationVO>> getNotifications(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Boolean read) {
        log.info("获取通知列表: page={}, size={}, read={}", page, size, read);
        return Result.success(favoriteService.getNotifications(page, size, read));
    }

    @PutMapping("/notifications/{notificationId}/read")
    @ApiOperation("标记通知已读")
    public Result<Void> markNotificationRead(@PathVariable Long notificationId) {
        log.info("标记通知已读: id={}", notificationId);
        favoriteService.markNotificationRead(notificationId);
        return Result.success();
    }

    @GetMapping("/notifications/unread-count")
    @ApiOperation("获取未读通知数")
    public Result<Integer> getUnreadCount() {
        log.info("获取未读通知数");
        return Result.success(favoriteService.getUnreadCount());
    }
}
