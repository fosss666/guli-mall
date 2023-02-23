package com.fosss.gulimall.product.dao;

import com.fosss.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {
    /**
     * 根据attrId查询可被检索的属性id
     *
     * @param canBeSearch
     */
    List<Long> getCanSearchAttrIds(@Param("attrIds") List<Long> attrIds, @Param("canBeSearch") int canBeSearch);
}
