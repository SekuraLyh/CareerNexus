package com.cn.DTO;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FavoriteRequestDTO {
    @NotBlank(message = "targetType 不能为空")
    private String targetType;   // JOB_SEEKER / ENTERPRISE / JOB

    @NotNull(message = "targetId 不能为空")
    private Long targetId;
}
