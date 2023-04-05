package com.fosss.gulimall.product.dao;

import com.fosss.gulimall.product.entity.AttrGroupEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fosss.gulimall.product.vo.SpuItemAttrGroupVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性分组
 * 
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@Mapper
public interface AttrGroupDao extends BaseMapper<AttrGroupEntity> {
    /**
     * 获取spu的规格参数信息
     */
    List<SpuItemAttrGroupVo> getSpuItemAttrGroup(@Param("spuId") Long spuId, @Param("catalogId") Long catalogId);
}
