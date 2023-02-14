package com.fosss.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.fosss.gulimall.ware.vo.MergeVo;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.ware.dao.PurchaseDao;
import com.fosss.gulimall.ware.entity.PurchaseEntity;
import com.fosss.gulimall.ware.service.PurchaseService;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    /**
     * 查询未领取的采购单
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseEntity> wrapper = new LambdaQueryWrapper();
        wrapper.eq(PurchaseEntity::getStatus, 0).or().eq(PurchaseEntity::getStatus, 1);
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    /**
     * 合并采购需求
     * purchaseId: 1, //整单id
     * items:[1,2,3,4] //合并项集合
     *
     * @param mergeVo
     */
    @Override
    public void merge(MergeVo mergeVo) {

    }

}
























