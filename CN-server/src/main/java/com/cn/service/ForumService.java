package com.cn.service;

import com.cn.DTO.ForumCommentRequestDTO;
import com.cn.DTO.ForumPostRequestDTO;
import com.cn.VO.CommentVO;
import com.cn.VO.LikeToggleVO;
import com.cn.VO.PostDetailVO;
import com.cn.VO.PostVO;
import com.cn.entity.ForumCategory;
import com.cn.result.PageResult;

import java.util.List;

public interface ForumService {

    /** 获取所有论坛分类 */
    List<ForumCategory> getCategories();

    /** 创建论坛分类（管理员） */
    void createCategory(String name, String description);

    /** 分页获取帖子列表 */
    PageResult<PostVO> getPosts(Integer page, Integer size, Long categoryId, String keyword, String sortBy);

    /** 获取帖子详情（含评论） */
    PostDetailVO getPostDetail(Long postId);

    /** 发布帖子 */
    PostVO createPost(ForumPostRequestDTO dto);

    /** 编辑帖子 */
    PostVO updatePost(Long postId, ForumPostRequestDTO dto);

    /** 删除帖子 */
    void deletePost(Long postId);

    /** 点赞/取消点赞 */
    LikeToggleVO toggleLike(Long postId);

    /** 发表评论 */
    CommentVO createComment(Long postId, ForumCommentRequestDTO dto);

    /** 删除评论 */
    void deleteComment(Long commentId);

    /** 获取我的帖子 */
    PageResult<PostVO> getMyPosts(Long userId, Integer page, Integer size);

    /** 管理员分页获取所有帖子 */
    PageResult<PostVO> listAllPosts(Integer page, Integer size, String keyword);

    /** 管理员删除帖子 */
    void adminDeletePost(Long postId);

    /** 管理员删除评论 */
    void adminDeleteComment(Long commentId);

    /** 获取用户发布的帖子 */
    PageResult<PostVO> getUserPosts(Long userId, Integer page, Integer size);
}
