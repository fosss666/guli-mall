package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.ProductAttrValueEntity;

import java.util.List;
import java.util.Map;

/**
 * spu属性值
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 获取spu规格
     */
    List<ProductAttrValueEntity> getSpuAttrValue(Long spuId);
    /**
     * 更新spu规格
     */
    void updateSpuAttrValue(Long spuId, List<ProductAttrValueEntity> productAttrValueEntities);
}

