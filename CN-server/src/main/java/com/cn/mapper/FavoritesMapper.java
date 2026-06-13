package com.cn.mapper;

import com.cn.VO.FavoriteVO;
import com.cn.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavoritesMapper {

    /** 插入收藏 */
    void insert(Favorite favorite);

    /** 按ID删除 */
    void deleteById(@Param("id") Long id);

    /** 按订阅者+目标删除（取消收藏） */
    void deleteByTarget(@Param("subscriberId") Long subscriberId,
                        @Param("targetType") String targetType,
                        @Param("targetId") Long targetId);

    /** 查询我的收藏列表（带目标摘要），可按类型筛选 */
    List<FavoriteVO> getBySubscriberId(@Param("subscriberId") Long subscriberId,
                                        @Param("targetType") String targetType);

    /** 按ID查询 */
    Favorite selectById(@Param("id") Long id);

    /** 检查是否已收藏 */
    Integer countByTarget(@Param("subscriberId") Long subscriberId,
                          @Param("targetType") String targetType,
                          @Param("targetId") Long targetId);

    /** 按目标查询收藏记录（用于通知时获取收藏者信息） */
    Favorite selectByTarget(@Param("targetType") String targetType,
                            @Param("targetId") Long targetId);
}
