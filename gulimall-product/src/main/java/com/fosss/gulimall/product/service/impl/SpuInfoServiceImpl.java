package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.fosss.common.to.MemberPrice;
import com.fosss.common.to.SkuReductionTo;
import com.fosss.common.to.SpuBoundTo;
import com.fosss.common.to.es.SkuEsModel;
import com.fosss.common.utils.R;
import com.fosss.gulimall.product.entity.*;
import com.fosss.gulimall.product.feign.CouponFeignService;
import com.fosss.gulimall.product.feign.WareFeignService;
import com.fosss.gulimall.product.service.*;
import com.fosss.gulimall.product.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    @Resource
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Resource
    private SkuImagesService skuImagesService;
    @Resource
    private CouponFeignService couponFeignService;
    @Resource
    private BrandService brandService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private WareFeignService wareFeignService;

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

        //5.保存spu的积分信息 gulimall_sms->sms_spu_bounds
        //远程调用gulimall-coupon中的保存方法
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        spuBoundTo.setBuyBounds(bounds.getBuyBounds());
        spuBoundTo.setGrowBounds(bounds.getGrowBounds());
        if (bounds.getBuyBounds().compareTo(new BigDecimal(0)) > 0 && bounds.getGrowBounds().compareTo(new BigDecimal(0)) > 0) {
            R r = couponFeignService.save(spuBoundTo);
            if (r.getCode() != 0) {
                log.error("远程调用方法保存spu积分信息失败");
            }
        }
        //6.保存spu的sku信息
        List<Skus> skus = spuSaveVo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach((sku) -> {
                //6.1保存sku的基本信息 pms_sku_info
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuName(sku.getSkuName());
                skuInfoEntity.setSkuTitle(sku.getSkuTitle());
                skuInfoEntity.setPrice(sku.getPrice());
                skuInfoEntity.setSkuSubtitle(sku.getSkuSubtitle());
                skuInfoEntity.setBrandId(spuSaveVo.getBrandId());
                skuInfoEntity.setCatalogId(spuSaveVo.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                //获取默认图片
                String defaultImage = "";
                List<Images> skuImages = sku.getImages();
                for (Images skuImage : skuImages) {
                    if (skuImage.getDefaultImg() == 1) {
                        defaultImage = skuImage.getImgUrl();
                    }
                }
                skuInfoEntity.setSkuDefaultImg(defaultImage);
                skuInfoService.save(skuInfoEntity);

                //6.2保存sku的销售属性 pms_sku_sale_attr_value
                List<Attr> attrList = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrList.stream().map((attr) -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                    skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                    skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);

                //6.3保存sku的图片信息 pms_sku_images
                List<SkuImagesEntity> skuImagesEntities = skuImages.stream()
                        .filter(item -> {//过滤掉没有url的数据
                            return item.getImgUrl() != null && item.getImgUrl().length() > 0;
                        })
                        .map((image -> {
                            SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                            skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                            skuImagesEntity.setImgUrl(image.getImgUrl());
                            skuImagesEntity.setDefaultImg(image.getDefaultImg());
                            return skuImagesEntity;
                        })).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);

                //6.4保存sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                skuReductionTo.setSkuId(skuInfoEntity.getSkuId());
                skuReductionTo.setCountStatus(sku.getCountStatus());
                skuReductionTo.setDiscount(sku.getDiscount());
                skuReductionTo.setFullCount(sku.getFullCount());
                skuReductionTo.setReducePrice(sku.getReducePrice());
                skuReductionTo.setFullPrice(sku.getFullPrice());
                skuReductionTo.setPriceStatus(sku.getPriceStatus());
                List<MemberPrice> memberPrice = new ArrayList<>();
                BeanUtils.copyProperties(sku.getMemberPrice(), memberPrice);
                skuReductionTo.setMemberPrice(memberPrice);
                R r1 = couponFeignService.saveReduction(skuReductionTo);
                if (r1.getCode() != 0) {
                    log.error("远程保存优惠满减失败");
                }
            });


        }
    }

    /**
     * spu检索
     */
    @Override
    public PageUtils listByConditions(Map<String, Object> params) {
        long page = Long.parseLong(params.get("page") + "");
        long limit = Long.parseLong(params.get("limit") + "");
        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String status = (String) params.get("status");

        IPage<SpuInfoEntity> iPage = new Page<>(page, limit);

        LambdaQueryWrapper<SpuInfoEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(!StringUtils.isEmpty(catelogId) && !"0".equals(catelogId), SpuInfoEntity::getCatalogId, catelogId)
                .eq(!StringUtils.isEmpty(brandId) && !"0".equals(brandId), SpuInfoEntity::getBrandId, brandId)
                .eq(!StringUtils.isEmpty(status), SpuInfoEntity::getPublishStatus, status)
                .like(!StringUtils.isEmpty(key), SpuInfoEntity::getId, key)
                .or().like(!StringUtils.isEmpty(key), SpuInfoEntity::getSpuName, key)
                .or().like(!StringUtils.isEmpty(key), SpuInfoEntity::getSpuDescription, key);

        baseMapper.selectPage(iPage, wrapper);
        return new PageUtils(iPage);
    }

    /**
     * 商品上架
     * /product/spuinfo/{spuId}/up
     */
    @Transactional
    @Override
    public void up(Long spuId) {
        //根据spuId查询sku
        List<SkuInfoEntity> skus = skuInfoService.list(new LambdaQueryWrapper<SkuInfoEntity>().eq(SkuInfoEntity::getSpuId, spuId));

        //查询spu的可以进行检索的规格属性 attrId attrName attrValue
        List<ProductAttrValueEntity> spuAttrValue = productAttrValueService.getSpuAttrValue(spuId);
        //根据attrId查询可被检索的属性
        List<Long> attrIds = spuAttrValue.stream().map(item -> item.getAttrId()).collect(Collectors.toList());
        List<Long> canSearchAttrIds = attrService.getCanSearchAttrIds(attrIds);
        //转为set
        Set<Long> set = new HashSet<>(canSearchAttrIds);
        //从spuAttrValue过滤可检索的attr
        List<SkuEsModel.Attr> attrs = spuAttrValue.stream().filter(item -> set.contains(item.getAttrId())).map((item) -> {
            SkuEsModel.Attr attr = new SkuEsModel.Attr();
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());

        //获取skuId
        List<Long> skuIds = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
        //查询各个sku是否有库存
        Map<Long, Boolean> hasStockMap = null;
        Boolean flag = false;
        try {
            hasStockMap = wareFeignService.hasStock(skuIds);
            flag = true;
        } catch (Exception e) {
            log.error("远程调用查询是否有库存失败：" + e.getMessage());
        }

        //构建统一类型
        Map<Long, Boolean> finalHasStockMap = hasStockMap;
        Boolean finalFlag = flag;
        List<SkuEsModel> collect = skus.stream().map((sku) -> {
            SkuEsModel skuEsModel = new SkuEsModel();
            //拷贝相同名称的属性
            BeanUtils.copyProperties(sku, skuEsModel);
            //设置其他属性 skuPrice skuImage
            skuEsModel.setSkuPrice(sku.getPrice());
            skuEsModel.setSkuImg(sku.getSkuDefaultImg());

            //设置是否有库存 hasStock
            if (finalFlag) {
                //远程调用成功
                skuEsModel.setHasStock(finalHasStockMap.get(sku.getSkuId()));
            } else {
                //远程调用失败，默认有库存
                skuEsModel.setHasStock(true);
            }

            //设置初始热度评分 hotScore
            skuEsModel.setHotScore(0L);

            // brandName; brandImg; catalogName;
            BrandEntity brand = brandService.getById(sku.getBrandId());
            skuEsModel.setBrandName(brand.getName());
            skuEsModel.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(sku.getCatalogId());
            skuEsModel.setCatalogName(category.getName());

            //设置可检索的规格属性
            skuEsModel.setAttrs(attrs);

            //返回映射对象
            return skuEsModel;
        }).collect(Collectors.toList());

        // TODO 4.远程调用微服务向es发送数据

    }
}





















