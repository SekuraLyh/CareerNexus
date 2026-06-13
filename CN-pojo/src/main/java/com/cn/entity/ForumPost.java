package com.cn.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ForumPost {
    private Long id;
    private Long categoryId;
    private Long userId;
    private String title;
    private String content;
    private Integer likeCount;
    private Integer commentCount;
    private Integer isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
