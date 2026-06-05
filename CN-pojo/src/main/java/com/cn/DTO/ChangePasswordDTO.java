package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("修改密码请求")
public class ChangePasswordDTO {

    @ApiModelProperty(value = "旧密码", required = true, example = "123456")
    private String oldPassword;

    @ApiModelProperty(value = "新密码", required = true, example = "newPassword123")
    private String newPassword;
}
