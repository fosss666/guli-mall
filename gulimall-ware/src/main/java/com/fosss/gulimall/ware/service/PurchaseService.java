package com.fosss.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.ware.entity.PurchaseEntity;
import com.fosss.gulimall.ware.vo.MergeVo;

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
}

