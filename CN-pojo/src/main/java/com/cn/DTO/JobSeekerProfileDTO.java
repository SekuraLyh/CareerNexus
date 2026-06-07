package com.cn.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
@ApiModel("求职者档案")
public class JobSeekerProfileDTO {
    
    @ApiModelProperty("档案ID")
    private Integer id;

    @ApiModelProperty("关联用户ID")
    private Integer userId;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("性别：MALE/FEMALE")
    private String gender;

    @ApiModelProperty("出生日期")
    private LocalDate birthDate;

    @ApiModelProperty("专业")
    private String major;

    @ApiModelProperty("最高学历：HIGH_SCHOOL/ASSOCIATE/BACHELOR/MASTER/PHD")
    private String education;

    @ApiModelProperty("毕业院校")
    private String school;

    @ApiModelProperty("毕业年份")
    private Integer graduationYear;

    @ApiModelProperty("工作年限（年）")
    private Integer workExperience;

    @ApiModelProperty("期望月薪（元）")
    private Integer expectedSalary;

    @ApiModelProperty("技能标签，逗号分隔")
    private String skills;

    @ApiModelProperty("自我描述")
    private String selfDescription;

    @ApiModelProperty("简历文件路径")
    private String resumeUrl;
}
