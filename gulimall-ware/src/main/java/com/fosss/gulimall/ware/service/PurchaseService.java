package com.fosss.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.ware.entity.PurchaseEntity;
import com.fosss.gulimall.ware.vo.MergeVo;
import com.fosss.gulimall.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:38:38
 */
public interface PurchaseService extends IService<PurchaseEntity> {
    /**
     * 查询未领取的采购单
     */
    PageUtils queryPage(Map<String, Object> params);
    /**
     * 合并采购需求
     * @param mergeVo
     */
    void merge(MergeVo mergeVo);
    /**
     * 查询采购单
     */
    PageUtils list(Map<String, Object> params);
    /**
     * 领取采购单
     * @param purchaseIds 采购单id
     */
    void received(List<Long> purchaseIds);
    /**
     * 完成采购功能
     */
    void donePurchase(PurchaseDoneVo purchaseDoneVo);
}

