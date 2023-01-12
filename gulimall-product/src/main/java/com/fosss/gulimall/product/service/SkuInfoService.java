package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

