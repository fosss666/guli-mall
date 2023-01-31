package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.vo.AttrVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Long catelogId, Map<String, Object> params);
    /**
     * 保存属性
     */
    void saveAttr(AttrVo attr);
}

