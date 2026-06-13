package com.cn.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumComment {
    private Long id;
    private Long postId;
    private Long userId;
    private String content;
    private Integer isDeleted;
    private LocalDateTime createdAt;
}
