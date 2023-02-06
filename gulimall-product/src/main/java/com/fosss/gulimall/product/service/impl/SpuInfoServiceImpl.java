package com.fosss.gulimall.product.service.impl;

import com.fosss.gulimall.product.entity.*;
import com.fosss.gulimall.product.service.*;
import com.fosss.gulimall.product.vo.BaseAttrs;
import com.fosss.gulimall.product.vo.Images;
import com.fosss.gulimall.product.vo.Skus;
import com.fosss.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescService spuInfoDescService;
    @Resource
    private SpuImagesService spuImagesService;
    @Resource
    private ProductAttrValueService productAttrValueService;
    @Resource
    private AttrService attrService;
    @Resource
    private SkuInfoService skuInfoService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存spu完整信息
     */
    @Transactional
    @Override
    public void saveSpu(SpuSaveVo spuSaveVo) {
        //1.保存spu基本信息 pms_spu_info
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        baseMapper.insert(spuInfoEntity);

        //2.保存spu描述图片 pms_spu_info_desc
        List<String> descriptImages = spuSaveVo.getDecript();
        String join = String.join(",", descriptImages);
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(join);
        spuInfoDescService.save(spuInfoDescEntity);

        //3.保存spu图集 pms_spu_images
        List<String> images = spuSaveVo.getImages();
        List<SpuImagesEntity> imageList = images.stream().map((image) -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(image);
            spuImagesEntity.setImgName(UUID.randomUUID().toString() + new Date());
            return spuImagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(imageList);

        //4.保存spu的规格参数 pms_product_attr_value
        List<BaseAttrs> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntities = baseAttrs.stream().map((item) -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(item.getAttrId());
            productAttrValueEntity.setAttrValue(item.getAttrValues());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            //需要从数据库中查名字
            productAttrValueEntity.setAttrName(attrService.getById(item.getAttrId()).getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntities);

        //TODO 5.保存spu的积分信息 gulimall_sms->sms_spu_bounds

        //6.保存spu的sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        skus.forEach((sku)->{
            //6.1保存sku的基本信息 pms_sku_info
            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            skuInfoEntity.setSkuName(sku.getSkuName());
            skuInfoEntity.setSkuTitle(sku.getSkuTitle());
            skuInfoEntity.setPrice(sku.getPrice());
            skuInfoEntity.setSkuSubtitle(sku.getSkuSubtitle());
            skuInfoEntity.setBrandId(spuSaveVo.getBrandId());
            skuInfoEntity.setCatalogId(spuSaveVo.getCatalogId());
            skuInfoEntity.setSaleCount(0L);
            //获取默认图片
            String defaultImage="";
            List<Images> skuImages = sku.getImages();
            for (Images skuImage : skuImages) {
                if(skuImage.getDefaultImg()==1){
                    defaultImage = skuImage.getImgUrl();
                }
            }
            skuInfoEntity.setSkuDefaultImg(defaultImage);
            skuInfoService.save(skuInfoEntity);

        });

        //6.2保存sku的销售属性 pms_sku_sale_attr_value
        //6.3保存sku的图片信息 pms_sku_images
        //6.4保存sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
    }

}





















