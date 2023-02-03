package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.entity.AttrGroupEntity;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.vo.AttrGroupRelationVo;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    //PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取分类属性分组
     */
    PageUtils queryPageList(Map<String, Object> params, Long catelogId);

    /**
     * 信息
     */
    AttrGroupEntity getInfo(Long attrGroupId);

    /**
     * 获取分类完整路径
     */
    public LinkedList<Long> getCatelogPath(CategoryEntity categoryEntity, LinkedList<Long> list);
    /**
     * 获取关联属性
     */
    List<AttrEntity> getAttrRelation(Long attrgroupId);
    /**
     * 删除分组关联的属性
     * @param attrGroupRelationVo
     */
    void deleteAttrRelation(AttrGroupRelationVo[] attrGroupRelationVo);
    /**
     * 获取该分组未关联的属性，用于新增关联
     */
    PageUtils getNotRelation(Long attrgroupId, Map<String, Object> params);
}

