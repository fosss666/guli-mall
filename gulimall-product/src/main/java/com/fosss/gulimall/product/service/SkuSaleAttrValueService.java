package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.fosss.gulimall.product.vo.SkuItemSaleAttrVo;

import java.util.List;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取spu的销售属性组合
     */
    List<SkuItemSaleAttrVo> getSkuSaleAttrBySpuId(Long spuId);
    /**
     * 根据skuId查询pms_sku_sale_attr_value表中的信息
     */
    List<String> getAttrsAsStringList(Long skuId);

}

