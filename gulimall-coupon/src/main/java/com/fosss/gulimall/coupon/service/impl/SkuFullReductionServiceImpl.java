package com.fosss.gulimall.coupon.service.impl;

import com.fosss.common.to.MemberPrice;
import com.fosss.common.to.SkuReductionTo;
import com.fosss.gulimall.coupon.entity.MemberPriceEntity;
import com.fosss.gulimall.coupon.entity.SkuLadderEntity;
import com.fosss.gulimall.coupon.service.MemberPriceService;
import com.fosss.gulimall.coupon.service.SkuLadderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.coupon.dao.SkuFullReductionDao;
import com.fosss.gulimall.coupon.entity.SkuFullReductionEntity;
import com.fosss.gulimall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Resource
    private SkuLadderService skuLadderService;
    @Resource
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存优惠满减等信息    sms_sku_ladder\sms_sku_full_reduction\sms_member_price
     */
    @Transactional
    @Override
    public void saveReduction(SkuReductionTo skuReductionTo) {
        //sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuReductionTo.getDiscount().compareTo(new BigDecimal(0)) > 0 && skuReductionTo.getFullCount() > 0) {
            skuLadderService.save(skuLadderEntity);
        }

        //sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
        skuFullReductionEntity.setFullPrice(skuReductionTo.getFullPrice());
        skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
        skuFullReductionEntity.setReducePrice(skuReductionTo.getReducePrice());
        if (skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) > 0 && skuReductionTo.getReducePrice().compareTo(new BigDecimal(0)) > 0) {
            baseMapper.insert(skuFullReductionEntity);
        }

        //sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntityList = memberPrice.stream().map((item -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        })).filter((item) -> {
            return item.getMemberPrice().compareTo(new BigDecimal(0)) > 0;
        }).collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntityList);

    }

}













