package com.fosss.gulimall.product.service.impl;

import com.fosss.gulimall.product.entity.SkuImagesEntity;
import com.fosss.gulimall.product.entity.SpuInfoDescEntity;
import com.fosss.gulimall.product.entity.SpuInfoEntity;
import com.fosss.gulimall.product.service.*;
import com.fosss.gulimall.product.vo.SeckillSkuVo;
import com.fosss.gulimall.product.vo.SkuItemSaleAttrVo;
import com.fosss.gulimall.product.vo.SkuItemVo;
import com.fosss.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.SkuInfoDao;
import com.fosss.gulimall.product.entity.SkuInfoEntity;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private AttrGroupService attrGroupService;
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private ThreadPoolExecutor executor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * sku检索
     */
    @Override
    public PageUtils listByConditions(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        /**
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {

            queryWrapper.eq("catalog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");

        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);

                //忽略掉max=0的条件
                if (bigDecimal.compareTo(new BigDecimal("0")) > 0) {
                    queryWrapper.le("price", max);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    /**
     * 查询该商品详情
     */
    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {
        SkuItemVo skuItemVo = new SkuItemVo();
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            //1、sku基本信息的获取  pms_sku_info
            SkuInfoEntity info = getById(skuId);
            skuItemVo.setInfo(info);
            return info;
        }, executor);
        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //3、获取spu的销售属性组合
            Long spuId = res.getSpuId();
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSkuSaleAttrBySpuId(spuId);
            skuItemVo.setSaleAttr(saleAttrVos);
        });
        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            //4、获取spu的介绍
            SpuInfoDescEntity desc = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setDesc(desc);
        });
        CompletableFuture<Void> groupAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            //5、获取spu的规格参数信息
            Long catalogId = res.getCatalogId();
            List<SpuItemAttrGroupVo> groupAttrs = attrGroupService.getSpuItemAttrGroup(res.getSpuId(), catalogId);
            skuItemVo.setGroupAttrs(groupAttrs);
        });

        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {
            //2、sku的图片信息    pms_sku_images
            List<SkuImagesEntity> images = skuImagesService.getSkuImagesBySkuId(skuId);
            skuItemVo.setImages(images);
        });

        //进行异步编排
        CompletableFuture.allOf(imageFuture, saleAttrFuture, descFuture, groupAttrFuture).get();

        return skuItemVo;
    }

}





















