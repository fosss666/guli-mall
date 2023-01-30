package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;
import com.fosss.gulimall.product.dao.BrandDao;
import com.fosss.gulimall.product.dao.CategoryBrandRelationDao;
import com.fosss.gulimall.product.dao.CategoryDao;
import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.entity.CategoryBrandRelationEntity;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandDao brandDao;
    @Resource
    private CategoryDao categoryDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String brandId = (String) params.get("brandId");
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId, brandId)
        );

        return new PageUtils(page);
    }

    /**
     * 保存
     */
    @Override
    public void saveDetails(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        BrandEntity brandEntity = brandDao.selectById(brandId);
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());
        baseMapper.insert(categoryBrandRelation);
    }

    /**
     * 更新品牌的时候同步更新品牌分类关系表
     *
     * @param brandId
     * @param name
     */
    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        baseMapper.update(
                categoryBrandRelationEntity,
                new LambdaUpdateWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId, brandId));
    }

    /**
     * 删除品牌时同步删除品牌分类关系表中的数据
     *
     * @param id
     */
    @Override
    public void deleteBrand(List<Long> id) {
        baseMapper.delete(new LambdaUpdateWrapper<CategoryBrandRelationEntity>().in(CategoryBrandRelationEntity::getBrandId, id));
    }

    /**
     * 删除品牌分类关系表中的数据
     *
     * @param catIds
     */
    @Override
    public void deleteCategories(List<Long> catIds) {
        LambdaUpdateWrapper<CategoryBrandRelationEntity> wrapper = new LambdaUpdateWrapper<>();
        wrapper.in(CategoryBrandRelationEntity::getCatelogId, catIds);
        baseMapper.delete(wrapper);
    }

    /**
     * 修改
     *
     * @param catId
     * @param name
     */
    @Override
    public void updateDetails(Long catId, String name) {
        baseMapper.updateCategory(catId, name);
    }

}











