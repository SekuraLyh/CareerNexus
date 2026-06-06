package com.cn.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 通用分页查询结果封装类
 * 适用于所有分页场景
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    private long total;
    
    private List<T> records;
    
    private Integer pageNum;
    
    private Integer pageSize;
    
    private Integer totalPages;

    /**
     * 构建空的分页结果
     */
    public static <T> PageResult<T> empty() {
        return PageResult.<T>builder()
                .total(0L)
                .records(Collections.emptyList())
                .pageNum(1)
                .pageSize(10)
                .totalPages(0)
                .build();
    }
}
