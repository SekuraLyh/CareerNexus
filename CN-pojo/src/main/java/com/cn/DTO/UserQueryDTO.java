package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("用户查询请求")
public class UserQueryDTO {

    @ApiModelProperty(value = "页码", example = "1")
    private Integer page = 1;

    @ApiModelProperty(value = "每页大小", example = "10")
    private Integer size = 10;

    @ApiModelProperty(value = "用户类型", example = "JOB_SEEKER")
    private String userType;

    @ApiModelProperty(value = "账号状态", example = "ACTIVE")
    private String status;

    @ApiModelProperty(value = "搜索关键词", example = "张三")
    private String keyword;
}
