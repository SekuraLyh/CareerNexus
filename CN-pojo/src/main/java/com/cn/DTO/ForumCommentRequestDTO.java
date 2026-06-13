package com.cn.DTO;

import lombok.Data;
import javax.validation.constraints.NotBlank;

@Data
public class ForumCommentRequestDTO {
    @NotBlank(message = "评论内容不能为空")
    private String content;
}
