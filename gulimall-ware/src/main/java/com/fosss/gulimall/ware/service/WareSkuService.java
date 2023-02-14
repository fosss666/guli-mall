package com.fosss.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.ware.entity.WareSkuEntity;

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
}

