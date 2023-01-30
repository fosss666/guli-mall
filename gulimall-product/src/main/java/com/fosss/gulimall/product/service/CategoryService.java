package com.fosss.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fosss.common.utils.PageUtils;
import com.fosss.gulimall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
public interface CategoryService extends IService<CategoryEntity> {

    /**
     * 查询所有的分类，并组装成树形(父子)结构返回
     */
    List<CategoryEntity> listTree();
    /**
     * 逻辑删除
     */
    void removeMenu(List<Long> catIds);

    /**
     * 修改
     * @param category
     */
    void updateDetails(CategoryEntity category);
}

