package com.fosss.gulimall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.ware.entity.PurchaseDetailEntity;

import java.util.Map;

/**
 * 
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:38:38
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {
    /**
     * 查询采购需求
     *    key: '华为',//检索关键字
     *    status: 0,//状态
     *    wareId: 1,//仓库id
     */
    PageUtils queryPage(Map<String, Object> params);
}

