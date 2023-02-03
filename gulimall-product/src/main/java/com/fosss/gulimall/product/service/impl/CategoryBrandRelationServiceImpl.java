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
import java.util.stream.Collectors;


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
        //防止重复关联
        LambdaQueryWrapper<CategoryBrandRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CategoryBrandRelationEntity::getBrandId, brandId).eq(CategoryBrandRelationEntity::getCatelogId, catelogId);
        Integer count = baseMapper.selectCount(wrapper);
        if (count == 0) {
            BrandEntity brandEntity = brandDao.selectById(brandId);
            CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
            categoryBrandRelation.setBrandName(brandEntity.getName());
            categoryBrandRelation.setCatelogName(categoryEntity.getName());
            baseMapper.insert(categoryBrandRelation);
        }

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

    /**
     * 查询分类关联的品牌
     */
    @Override
    public List<BrandEntity> getBrandsByCid(Long catId) {
        //查询该分类关联的品牌id
        List<CategoryBrandRelationEntity> relationEntities = baseMapper.selectList(new LambdaUpdateWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getCatelogId, catId));
        List<Long> brandIds = relationEntities.stream().map((item) -> {
            return item.getBrandId();
        }).collect(Collectors.toList());
        //获取品牌
        List<BrandEntity> brandEntities = brandIds.stream().map((id) -> {
            return brandDao.selectById(id);
        }).collect(Collectors.toList());
        return brandEntities;
    }

}











