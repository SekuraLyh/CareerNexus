package com.cn.controller.auth;

import com.cn.DTO.ForumCommentRequestDTO;
import com.cn.DTO.ForumPostRequestDTO;
import com.cn.VO.CommentVO;
import com.cn.VO.LikeToggleVO;
import com.cn.VO.PostDetailVO;
import com.cn.VO.PostVO;
import com.cn.context.BaseContext;
import com.cn.entity.ForumCategory;
import com.cn.result.PageResult;
import com.cn.result.Result;
import com.cn.service.ForumService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/forum")
@Slf4j
@Api(tags = "论坛接口")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @GetMapping("/categories")
    @ApiOperation("获取论坛分类列表")
    public Result<List<ForumCategory>> getCategories() {
        return Result.success(forumService.getCategories());
    }

    @PostMapping("/categories")
    @ApiOperation("创建论坛分类")
    public Result createCategory(@RequestParam String name,
                                  @RequestParam(required = false) String description) {
        log.info("创建分类: name={}", name);
        forumService.createCategory(name, description);
        return Result.success();
    }

    @GetMapping("/posts")
    @ApiOperation("获取帖子列表")
    public Result<PageResult<PostVO>> getPosts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "latest") String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("获取帖子列表: categoryId={}, keyword={}, sortBy={}, page={}, size={}", categoryId, keyword, sortBy, page, size);
        return Result.success(forumService.getPosts(page, size, categoryId, keyword, sortBy));
    }

    @GetMapping("/posts/{postId}")
    @ApiOperation("获取帖子详情")
    public Result<PostDetailVO> getPostDetail(@PathVariable Long postId) {
        log.info("获取帖子详情: postId={}", postId);
        return Result.success(forumService.getPostDetail(postId));
    }

    @PostMapping("/posts")
    @ApiOperation("发布帖子")
    public Result<PostVO> createPost(@Valid @RequestBody ForumPostRequestDTO dto) {
        log.info("发布帖子: title={}", dto.getTitle());
        return Result.success(forumService.createPost(dto));
    }

    @PutMapping("/posts/{postId}")
    @ApiOperation("编辑帖子")
    public Result<PostVO> updatePost(@PathVariable Long postId,
                                      @Valid @RequestBody ForumPostRequestDTO dto) {
        log.info("编辑帖子: postId={}", postId);
        return Result.success(forumService.updatePost(postId, dto));
    }

    @DeleteMapping("/posts/{postId}")
    @ApiOperation("删除帖子")
    public Result deletePost(@PathVariable Long postId) {
        log.info("删除帖子: postId={}", postId);
        forumService.deletePost(postId);
        return Result.success();
    }

    @PostMapping("/posts/{postId}/like")
    @ApiOperation("点赞/取消点赞帖子")
    public Result<LikeToggleVO> likePost(@PathVariable Long postId) {
        log.info("点赞帖子: postId={}", postId);
        return Result.success(forumService.toggleLike(postId));
    }

    @PostMapping("/posts/{postId}/comments")
    @ApiOperation("评论帖子")
    public Result<CommentVO> createComment(@PathVariable Long postId,
                                            @Valid @RequestBody ForumCommentRequestDTO dto) {
        log.info("评论帖子: postId={}", postId);
        return Result.success(forumService.createComment(postId, dto));
    }

    @DeleteMapping("/comments/{commentId}")
    @ApiOperation("删除评论")
    public Result deleteComment(@PathVariable Long commentId) {
        log.info("删除评论: commentId={}", commentId);
        forumService.deleteComment(commentId);
        return Result.success();
    }

    @GetMapping("/posts/my")
    @ApiOperation("获取我的帖子")
    public Result<PageResult<PostVO>> getMyPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        Long userId = BaseContext.getCurrentId();
        log.info("获取我的帖子: userId={}, page={}, size={}", userId, page, size);
        return Result.success(forumService.getMyPosts(userId, page, size));
    }

    @GetMapping("/users/{userId}/posts")
    @ApiOperation("获取用户发布的帖子")
    public Result<PageResult<PostVO>> getUserPosts(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        log.info("获取用户帖子: userId={}, page={}, size={}", userId, page, size);
        return Result.success(forumService.getUserPosts(userId, page, size));
    }
}
