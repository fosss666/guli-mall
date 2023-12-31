package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.vo.AttrRespVo;
import com.fosss.gulimall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Long catelogId, Map<String, Object> params, String type);
    /**
     * 保存属性
     */
    void saveAttr(AttrVo attr);
    /**
     * 查询属性详情
     */
    AttrRespVo getInfo(Long attrId);
    /**
     * 修改属性详情
     */
    void updateAttr(AttrVo attr);

    /**
     * 根据attrId查询可被检索的属性id
     */
    List<Long> getCanSearchAttrIds(List<Long> attrIds);
}

