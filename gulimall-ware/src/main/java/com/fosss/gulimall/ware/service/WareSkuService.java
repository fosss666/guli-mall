package com.fosss.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:38:38
 */
public interface WareSkuService extends IService<WareSkuEntity> {
    /**
     * 查询商品库存
     */
    PageUtils queryPage(Map<String, Object> params);
    /**
     * 添加库存
     * @param skuId skuId
     * @param wareId 仓库id
     * @param skuNum 当前库存
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);
    /**
     * 根据skuId查询是否有库存
     */
    Map<Long, Boolean> hasStock(List<Long> skuIds);
}

