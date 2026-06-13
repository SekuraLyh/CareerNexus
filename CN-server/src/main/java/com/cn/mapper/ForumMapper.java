package com.cn.mapper;

import com.cn.DTO.EnterpriseProfileDTO;
import com.cn.DTO.JobSeekerProfileDTO;
import com.cn.VO.CommentVO;
import com.cn.VO.PostVO;
import com.cn.entity.ForumCategory;
import com.cn.entity.ForumComment;
import com.cn.entity.ForumLike;
import com.cn.entity.ForumPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ForumMapper {

    // ==================== 分类 ====================

    List<ForumCategory> selectCategories();

    void insertCategory(ForumCategory category);

    // ==================== 帖子 ====================

    List<PostVO> selectPosts(@Param("categoryId") Long categoryId,
                             @Param("keyword") String keyword,
                             @Param("sortBy") String sortBy);

    /** 查询用户发布的帖子 */
    List<PostVO> selectPostsByUserId(@Param("userId") Long userId);

    PostVO selectPostById(@Param("postId") Long postId);

    ForumPost selectPostEntityById(@Param("postId") Long postId);

    void insertPost(ForumPost post);

    void updatePost(ForumPost post);

    void deletePostById(@Param("postId") Long postId);

    // ==================== 评论 ====================

    List<CommentVO> selectCommentsByPostId(@Param("postId") Long postId);

    void insertComment(ForumComment comment);

    void deleteCommentById(@Param("commentId") Long commentId);

    ForumComment selectCommentById(@Param("commentId") Long commentId);

    // ==================== 点赞 ====================

    ForumLike selectLikeByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    void insertLike(ForumLike like);

    void deleteLikeByPostAndUser(@Param("postId") Long postId, @Param("userId") Long userId);

    // ==================== 计数器 ====================

    void incrementCommentCount(@Param("postId") Long postId);

    void decrementCommentCount(@Param("postId") Long postId);

    void incrementLikeCount(@Param("postId") Long postId);

    void decrementLikeCount(@Param("postId") Long postId);

    // ==================== 用户档案查询 ====================

    JobSeekerProfileDTO getJobSeekerProfile(@Param("userId") Long userId);

    EnterpriseProfileDTO getEnterpriseProfile(@Param("userId") Long userId);
}
