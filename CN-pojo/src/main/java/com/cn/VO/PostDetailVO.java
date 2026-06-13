package com.cn.VO;

import com.cn.entity.ForumComment;
import com.cn.entity.ForumPost;
import lombok.Data;
import java.util.List;

@Data
public class PostDetailVO {
    private ForumPost post;
    private List<ForumComment> comments;
    /** 当前用户是否已点赞（未登录时为 null） */
    private Boolean liked;
}
