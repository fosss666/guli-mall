package com.fosss.gulimall.product.service.impl;

import com.fosss.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.CategoryDao;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 逻辑删除
     */
    @Override
    @Transactional
    public void removeMenu(List<Long> catIds) {
        //删除品牌分类关系表中的数据
        categoryBrandRelationService.deleteCategories(catIds);

        //实施删除
        baseMapper.deleteBatchIds(catIds);
    }

    /**
     * 修改,同时修改品牌分类关系表中的数据
     */
    @Override
    @Transactional
    public void updateDetails(CategoryEntity category) {
        baseMapper.updateById(category);
        categoryBrandRelationService.updateDetails(category.getCatId(), category.getName());
    }

    /**
     * 查询所有的分类，并组装成树形(父子)结构返回
     */
    @Override
    public List<CategoryEntity> listTree() {
        //查询所有分类
        List<CategoryEntity> categoryList = baseMapper.selectList(null);
        //封装树形结构
        List<CategoryEntity> tree = new ArrayList<>();
        //组装树形结构
        for (CategoryEntity category : categoryList) {
            //获取一级分类
            if (category.getParentCid().equals(0L)) {
                //为一级分类设置子分类及为子分类设置子分类--递归
                setChildrenCategory(category, categoryList);
                tree.add(category);
            }
        }
        return tree;
    }

    /**
     * 递归设置子分类
     *
     * @param parentCategory 父分类
     * @param categoryList   总分类
     */
    private void setChildrenCategory(CategoryEntity parentCategory, List<CategoryEntity> categoryList) {
        parentCategory.setChildren(new ArrayList<>());
        for (CategoryEntity category : categoryList) {
            if (category.getParentCid().equals(parentCategory.getCatId())) {
                parentCategory.getChildren().add(category);
                setChildrenCategory(category, categoryList);

                //将子分类按照sort进行升序排序
                if (parentCategory.getChildren() != null && parentCategory.getChildren().size() > 0) {
                    parentCategory.getChildren().sort(new Comparator<CategoryEntity>() {
                        @Override
                        public int compare(CategoryEntity o1, CategoryEntity o2) {
                            //升序排序
                            return o1.getSort() - o2.getSort();

                        }
                    });
                }
            }
        }

    }

}

















