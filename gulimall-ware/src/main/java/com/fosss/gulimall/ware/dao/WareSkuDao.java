package com.fosss.gulimall.ware.dao;

import com.fosss.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:38:38
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
    //查询该sku的库存量
    Long hasStock(@Param("skuId") Long skuId);
}
