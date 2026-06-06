package com.cn.VO;

import lombok.Data;

import java.util.ArrayList;

@Data
public class PaginatedUserVO {
    private Integer page;
    private Integer size;
    private Integer total;
    private ArrayList<UserVO> users;
}
