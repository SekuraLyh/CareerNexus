package com.cn.DTO;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ToggleEnabledDTO {
    @NotNull(message = "enabled不能为空")
    private Boolean enabled;
}
