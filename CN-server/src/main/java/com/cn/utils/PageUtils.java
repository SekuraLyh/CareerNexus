package com.cn.utils;

import com.github.pagehelper.PageInfo;
import com.cn.result.PageResult;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 * 提供通用的分页结果转换方法，提高代码复用性
 */
public class PageUtils {

    /**
     * 将 PageInfo 转换为 PageResult（无数据转换）
     *
     * @param pageInfo 分页信息
     * @return 分页结果
     */
    public static <T> PageResult<T> toPageResult(PageInfo<T> pageInfo) {
        return PageResult.<T>builder()
                .total(pageInfo.getTotal())
                .records(pageInfo.getList())
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .totalPages(pageInfo.getPages())
                .build();
    }

    /**
     * 将 PageInfo 转换为 PageResult（带数据转换）
     * 适用于 Entity -> VO 的转换场景
     *
     * @param pageInfo 分页信息
     * @param converter 数据转换函数
     * @return 分页结果
     */
    public static <S, T> PageResult<T> toPageResult(PageInfo<S> pageInfo, Function<S, T> converter) {
        List<T> convertedList = pageInfo.getList().stream()
                .map(converter)
                .collect(Collectors.toList());
        
        return PageResult.<T>builder()
                .total(pageInfo.getTotal())
                .records(convertedList)
                .pageNum(pageInfo.getPageNum())
                .pageSize(pageInfo.getPageSize())
                .totalPages(pageInfo.getPages())
                .build();
    }

    /**
     * 构建空的 PageResult
     *
     * @return 空的分页结果
     */
    public static <T> PageResult<T> emptyPageResult() {
        return PageResult.empty();
    }
}
