package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("企业档案")
public class EnterpriseProfileDTO {
    
    @ApiModelProperty("档案ID")
    private Integer id;

    @ApiModelProperty("关联用户ID")
    private Integer userId;

    @ApiModelProperty("公司名称")
    private String companyName;

    @ApiModelProperty("所属行业")
    private String industry;

    @ApiModelProperty("公司规模")
    private String companySize;

    @ApiModelProperty("公司简介")
    private String companyDescription;

    @ApiModelProperty("联系人")
    private String contactPerson;

    @ApiModelProperty("联系电话")
    private String contactPhone;

    @ApiModelProperty("公司地址")
    private String address;

    @ApiModelProperty("公司官网")
    private String website;
}
