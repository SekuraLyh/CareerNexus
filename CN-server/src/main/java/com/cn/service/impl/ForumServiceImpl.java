package com.cn.service.impl;

import com.cn.DTO.ForumCommentRequestDTO;
import com.cn.DTO.ForumPostRequestDTO;
import com.cn.VO.CommentVO;
import com.cn.VO.LikeToggleVO;
import com.cn.VO.PostDetailVO;
import com.cn.VO.PostVO;
import com.cn.context.BaseContext;
import com.cn.entity.ForumCategory;
import com.cn.entity.ForumComment;
import com.cn.entity.ForumLike;
import com.cn.entity.ForumPost;
import com.cn.entity.Notification;
import com.cn.exception.BusinessException;
import com.cn.mapper.ForumMapper;
import com.cn.mapper.NotificationsMapper;
import com.cn.result.PageResult;
import com.cn.service.ForumService;
import com.cn.service.UserSubscriptionService;
import com.cn.utils.PageUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ForumServiceImpl implements ForumService {

    @Autowired
    private ForumMapper forumMapper;

    @Autowired
    private NotificationsMapper notificationsMapper;

    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @Override
    public List<ForumCategory> getCategories() {
        return forumMapper.selectCategories();
    }

    @Override
    public void createCategory(String name, String description) {
        ForumCategory category = new ForumCategory();
        category.setName(name);
        category.setDescription(description);
        forumMapper.insertCategory(category);
    }

    @Override
    public PageResult<PostVO> getPosts(Integer page, Integer size, Long categoryId, String keyword, String sortBy) {
        PageHelper.startPage(page, size);
        List<PostVO> posts = forumMapper.selectPosts(categoryId, keyword, sortBy);
        PageInfo<PostVO> pageInfo = new PageInfo<>(posts);
        return PageUtils.toPageResult(pageInfo);
    }

    @Override
    public PostDetailVO getPostDetail(Long postId) {
        PostVO post = forumMapper.selectPostById(postId);
        if (post == null) {
            throw new BusinessException(404, "帖子不存在");
        }
        List<CommentVO> comments = forumMapper.selectCommentsByPostId(postId);
        PostDetailVO detail = new PostDetailVO();
        detail.setPost(convertToForumPost(post));
        detail.setComments(convertToForumComments(comments));
        
        // 检查当前用户是否已点赞
        try {
            Long currentId = BaseContext.getCurrentId();
            ForumLike like = forumMapper.selectLikeByPostAndUser(postId, currentId);
            detail.setLiked(like != null);
        } catch (Exception e) {
            // 未登录用户不设置 liked
            detail.setLiked(false);
        }
        
        return detail;
    }

    @Override
    @Transactional
    public PostVO createPost(ForumPostRequestDTO dto) {
        ForumPost post = new ForumPost();
        post.setCategoryId(dto.getCategoryId());
        post.setUserId(BaseContext.getCurrentId());
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setLikeCount(0);
        post.setCommentCount(0);
        forumMapper.insertPost(post);
        
        // 通知关注者
        userSubscriptionService.notifyFollowersOfNewPost(post.getUserId(), post.getId(), dto.getTitle());
        
        return forumMapper.selectPostById(post.getId());
    }

    @Override
    @Transactional
    public PostVO updatePost(Long postId, ForumPostRequestDTO dto) {
        ForumPost existing = forumMapper.selectPostEntityById(postId);
        if (existing == null) {
            throw new BusinessException(404, "帖子不存在");
        }
        Long currentId = BaseContext.getCurrentId();
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, "仅帖子作者可编辑");
        }
        existing.setTitle(dto.getTitle());
        existing.setContent(dto.getContent());
        existing.setCategoryId(dto.getCategoryId());
        forumMapper.updatePost(existing);
        return forumMapper.selectPostById(postId);
    }

    @Override
    @Transactional
    public void deletePost(Long postId) {
        ForumPost existing = forumMapper.selectPostEntityById(postId);
        if (existing == null) {
            throw new BusinessException(404, "帖子不存在");
        }
        Long currentId = BaseContext.getCurrentId();
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, "仅帖子作者可删除");
        }
        forumMapper.deletePostById(postId);
    }

    @Override
    @Transactional
    public LikeToggleVO toggleLike(Long postId) {
        Long currentId = BaseContext.getCurrentId();
        ForumLike existing = forumMapper.selectLikeByPostAndUser(postId, currentId);
        boolean liked;
        if (existing == null) {
            ForumLike like = new ForumLike();
            like.setPostId(postId);
            like.setUserId(currentId);
            forumMapper.insertLike(like);
            forumMapper.incrementLikeCount(postId);
            liked = true;
            
            // 发送点赞通知
            sendLikeNotification(postId, currentId);
        } else {
            forumMapper.deleteLikeByPostAndUser(postId, currentId);
            forumMapper.decrementLikeCount(postId);
            liked = false;
        }
        ForumPost post = forumMapper.selectPostEntityById(postId);
        LikeToggleVO vo = new LikeToggleVO();
        vo.setLiked(liked);
        vo.setLikeCount(post != null ? post.getLikeCount() : 0);
        return vo;
    }

    @Override
    @Transactional
    public CommentVO createComment(Long postId, ForumCommentRequestDTO dto) {
        ForumPost post = forumMapper.selectPostEntityById(postId);
        if (post == null) {
            throw new BusinessException(404, "帖子不存在");
        }
        Long currentId = BaseContext.getCurrentId();
        ForumComment comment = new ForumComment();
        comment.setPostId(postId);
        comment.setUserId(currentId);
        comment.setContent(dto.getContent());
        forumMapper.insertComment(comment);
        forumMapper.incrementCommentCount(postId);
        
        // 发送评论通知
        sendCommentNotification(postId, currentId);
        
        return forumMapper.selectCommentsByPostId(postId).stream()
                .filter(c -> c.getId().equals(comment.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(500, "评论创建失败"));
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        ForumComment existing = forumMapper.selectCommentById(commentId);
        if (existing == null) {
            throw new BusinessException(404, "评论不存在");
        }
        if (existing.getIsDeleted() == 1) {
            throw new BusinessException(400, "评论已被删除");
        }
        Long currentId = BaseContext.getCurrentId();
        if (!existing.getUserId().equals(currentId)) {
            throw new BusinessException(403, "仅评论作者可删除");
        }
        forumMapper.deleteCommentById(commentId);
        forumMapper.decrementCommentCount(existing.getPostId());
    }

    @Override
    public PageResult<PostVO> getMyPosts(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<PostVO> posts = forumMapper.selectPosts(null, null, null);
        List<PostVO> myPosts = posts.stream()
                .filter(p -> p.getUserId().equals(userId))
                .collect(Collectors.toList());
        PageInfo<PostVO> pageInfo = new PageInfo<>(posts);
        pageInfo.setList(myPosts);
        pageInfo.setTotal(myPosts.size());
        return PageUtils.toPageResult(pageInfo);
    }

    @Override
    public PageResult<PostVO> listAllPosts(Integer page, Integer size, String keyword) {
        PageHelper.startPage(page, size);
        List<PostVO> posts = forumMapper.selectPosts(null, keyword, null);
        PageInfo<PostVO> pageInfo = new PageInfo<>(posts);
        return PageUtils.toPageResult(pageInfo);
    }

    @Override
    @Transactional
    public void adminDeletePost(Long postId) {
        ForumPost existing = forumMapper.selectPostEntityById(postId);
        if (existing == null) {
            throw new BusinessException(404, "帖子不存在");
        }
        forumMapper.deletePostById(postId);
    }

    @Override
    @Transactional
    public void adminDeleteComment(Long commentId) {
        ForumComment existing = forumMapper.selectCommentById(commentId);
        if (existing == null) {
            throw new BusinessException(404, "评论不存在");
        }
        if (existing.getIsDeleted() == 1) {
            throw new BusinessException(400, "评论已被删除");
        }
        forumMapper.deleteCommentById(commentId);
        forumMapper.decrementCommentCount(existing.getPostId());
    }

    @Override
    public PageResult<PostVO> getUserPosts(Long userId, Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<PostVO> posts = forumMapper.selectPostsByUserId(userId);
        PageInfo<PostVO> pageInfo = new PageInfo<>(posts);
        return PageUtils.toPageResult(pageInfo);
    }

    private ForumPost convertToForumPost(PostVO vo) {
        ForumPost post = new ForumPost();
        post.setId(vo.getId());
        post.setCategoryId(vo.getCategoryId());
        post.setUserId(vo.getUserId());
        post.setTitle(vo.getTitle());
        post.setContent(vo.getContent());
        post.setLikeCount(vo.getLikeCount());
        post.setCommentCount(vo.getCommentCount());
        post.setCreatedAt(vo.getCreatedAt());
        return post;
    }

    private List<ForumComment> convertToForumComments(List<CommentVO> vos) {
        return vos.stream().map(vo -> {
            ForumComment comment = new ForumComment();
            comment.setId(vo.getId());
            comment.setPostId(vo.getPostId());
            comment.setUserId(vo.getUserId());
            comment.setContent(vo.getContent());
            comment.setCreatedAt(vo.getCreatedAt());
            return comment;
        }).collect(Collectors.toList());
    }

    /**
     * 发送点赞通知
     */
    private void sendLikeNotification(Long postId, Long likerId) {
        ForumPost post = forumMapper.selectPostEntityById(postId);
        if (post == null || post.getUserId().equals(likerId)) {
            return; // 不通知自己
        }
        
        String likerName = getUserName(likerId);
        Notification notification = new Notification();
        notification.setUserId(post.getUserId());
        notification.setType("POST_LIKED");
        notification.setRelatedId(postId);
        notification.setMessage(likerName + " 赞了你的帖子「" + post.getTitle() + "」");
        notification.setTargetUrl("/forum/posts/" + postId);
        notification.setIsRead(false);
        notificationsMapper.insert(notification);
        
        log.info("点赞通知已发送: postId={}, authorId={}, likerId={}", postId, post.getUserId(), likerId);
    }

    /**
     * 发送评论通知
     */
    private void sendCommentNotification(Long postId, Long commenterId) {
        ForumPost post = forumMapper.selectPostEntityById(postId);
        if (post == null || post.getUserId().equals(commenterId)) {
            return; // 不通知自己
        }
        
        String commenterName = getUserName(commenterId);
        Notification notification = new Notification();
        notification.setUserId(post.getUserId());
        notification.setType("POST_COMMENTED");
        notification.setRelatedId(postId);
        notification.setMessage(commenterName + " 回复了你的帖子「" + post.getTitle() + "」");
        notification.setTargetUrl("/forum/posts/" + postId);
        notification.setIsRead(false);
        notificationsMapper.insert(notification);
        
        log.info("评论通知已发送: postId={}, authorId={}, commenterId={}", postId, post.getUserId(), commenterId);
    }

    /**
     * 获取用户名称（根据用户类型）
     */
    private String getUserName(Long userId) {
        // 尝试从求职者档案获取
        com.cn.DTO.JobSeekerProfileDTO seeker = forumMapper.getJobSeekerProfile(userId);
        if (seeker != null && seeker.getRealName() != null) {
            return seeker.getRealName();
        }
        
        // 尝试从企业档案获取
        com.cn.DTO.EnterpriseProfileDTO enterprise = forumMapper.getEnterpriseProfile(userId);
        if (enterprise != null && enterprise.getCompanyName() != null) {
            return enterprise.getCompanyName();
        }
        
        // 默认返回用户名
        return "用户";
    }
}
