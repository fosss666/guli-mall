package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
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

    /**
     * 删除品牌时同步删除品牌分类关系表中的数据
     * @param id
     */
    void deleteBrand(List<Long> id);

    /**
     * 删除品牌分类关系表中的数据
     * @param catIds
     */
    void deleteCategories(List<Long> catIds);

    /**
     * 修改
     * @param catId
     * @param name
     */
    void updateDetails(Long catId, String name);
    /**
     * 查询分类关联的品牌
     */
    List<BrandEntity> getBrandsByCid(Long catId);
}

