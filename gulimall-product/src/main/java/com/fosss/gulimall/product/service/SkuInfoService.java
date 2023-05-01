package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.SkuInfoEntity;
import com.fosss.gulimall.product.vo.SkuItemVo;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * sku检索
     */
    PageUtils listByConditions(Map<String, Object> params);

    /**
     * 查询该商品详情
     */
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}

