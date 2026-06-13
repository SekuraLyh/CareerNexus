package com.cn.VO;

import lombok.Data;
import java.time.LocalDateTime;

/** 帖子列表项（含分类名和用户名） */
@Data
public class PostVO {
    private Long id;
    private Long categoryId;
    private String categoryName;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createdAt;
}
