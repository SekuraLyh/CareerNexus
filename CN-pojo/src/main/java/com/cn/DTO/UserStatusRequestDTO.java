package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户状态请求")
public class UserStatusRequestDTO {
    private String status;
}
