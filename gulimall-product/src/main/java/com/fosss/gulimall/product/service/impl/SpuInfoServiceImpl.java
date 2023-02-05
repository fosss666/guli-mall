package com.fosss.gulimall.product.service.impl;

import com.fosss.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.SpuInfoDao;
import com.fosss.gulimall.product.entity.SpuInfoEntity;
import com.fosss.gulimall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

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
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        baseMapper.insert(spuInfoEntity);

        //2.保存spu描述图片 pms_spu_info_desc
        //3.保存spu图集 pms_spu_images
        //4.保存spu的规格参数 pms_product_attr_value
        //5.保存spu的积分信息 gulimall_sms->sms_spu_bounds
        //6.保存spu的sku信息
        //6.1保存sku的基本信息 pms_sku_info
        //6.2保存sku的销售属性 pms_sku_sale_attr_value
        //6.3保存sku的图片信息 pms_sku_images
        //6.4保存sku的优惠、满减等信息；gulimall_sms->sms_sku_ladder\sms_sku_full_reduction\sms_member_price
    }

}





















