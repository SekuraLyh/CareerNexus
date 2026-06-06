package com.cn.DTO;

import lombok.Data;

@Data
public class GetUsersDTO {
    private Integer page;
    private Integer size;
    private String userType;
    private String status;
    private String keyword;
}
