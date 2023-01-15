package com.fosss.gulimall.product.service.impl;

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


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {


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

















