package com.cn.DTO;

import lombok.Data;

@Data
public class JobQueryDTO {
    private Integer page = 1;
    private Integer size = 10;
    private String keyword;
    private String major;
    private String location;
    private String minSalary;
}
