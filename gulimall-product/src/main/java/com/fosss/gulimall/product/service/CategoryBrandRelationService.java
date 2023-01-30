package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.CategoryBrandRelationEntity;

import javax.validation.constraints.Null;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);
    /**
     * 保存
     */
    void saveDetails(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 更新品牌的时候同步更新品牌分类关系表
     * @param brandId
     * @param name
     */
    void updateBrand( Long brandId,  String name);
}

