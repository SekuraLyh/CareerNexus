package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户登录请求")
public class JobSeekerLoginDTO {

    @ApiModelProperty(value = "用户名", required = true, example = "zhangsan")
    private String username;

    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;
}
