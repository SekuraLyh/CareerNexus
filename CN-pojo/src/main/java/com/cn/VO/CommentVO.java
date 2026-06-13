package com.cn.VO;

import lombok.Data;
import java.time.LocalDateTime;

/** 评论列表项（含用户名） */
@Data
public class CommentVO {
    private Long id;
    private Long postId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
}
