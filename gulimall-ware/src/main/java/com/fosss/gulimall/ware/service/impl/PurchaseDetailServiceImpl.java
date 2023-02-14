package com.fosss.gulimall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.ware.dao.PurchaseDetailDao;
import com.fosss.gulimall.ware.entity.PurchaseDetailEntity;
import com.fosss.gulimall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {
    /**
     * 查询采购需求
     * key: '华为',//检索关键字
     * status: 0,//状态
     * wareId: 1,//仓库id
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new LambdaQueryWrapper<>();
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w
                        .eq(PurchaseDetailEntity::getPurchaseId, key)
                        .or().eq(PurchaseDetailEntity::getSkuId, key)
                        .or().eq(PurchaseDetailEntity::getWareId, key)
                        .or().like(PurchaseDetailEntity::getSkuNum, key)
                        .or().like(PurchaseDetailEntity::getSkuPrice, key);
            });
        }
        wrapper
                .eq(!StringUtils.isEmpty(status), PurchaseDetailEntity::getStatus, status)
                .eq(!StringUtils.isEmpty(wareId), PurchaseDetailEntity::getWareId, wareId);
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}















