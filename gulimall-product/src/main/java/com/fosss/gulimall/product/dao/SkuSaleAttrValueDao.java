package com.fosss.gulimall.product.dao;

import com.fosss.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fosss.gulimall.product.vo.SkuItemSaleAttrVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * sku销售属性&值
 * 
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@Mapper
public interface SkuSaleAttrValueDao extends BaseMapper<SkuSaleAttrValueEntity> {
    /**
     * 获取spu的销售属性组合
     */
    List<SkuItemSaleAttrVo> getSkuSaleAttrBySpuId(@Param("spuId") Long spuId);
    /**
     * 根据skuId查询pms_sku_sale_attr_value表中的信息
     */
    List<String> getAttrsAsStringList(@Param("skuId") Long skuId);
}
