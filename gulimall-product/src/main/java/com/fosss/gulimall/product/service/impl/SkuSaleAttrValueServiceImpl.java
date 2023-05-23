package com.fosss.gulimall.product.service.impl;

import com.fosss.gulimall.product.vo.SkuItemSaleAttrVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.SkuSaleAttrValueDao;
import com.fosss.gulimall.product.entity.SkuSaleAttrValueEntity;
import com.fosss.gulimall.product.service.SkuSaleAttrValueService;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuSaleAttrValueEntity> page = this.page(
                new Query<SkuSaleAttrValueEntity>().getPage(params),
                new QueryWrapper<SkuSaleAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取spu的销售属性组合
     */
    @Override
    public List<SkuItemSaleAttrVo> getSkuSaleAttrBySpuId(Long spuId) {

        return baseMapper.getSkuSaleAttrBySpuId(spuId);
    }

    /**
     * 根据skuId查询pms_sku_sale_attr_value表中的信息
     */
    @Override
    public List<String> getAttrsAsStringList(Long skuId) {
        return baseMapper.getAttrsAsStringList(skuId);
    }

}

















