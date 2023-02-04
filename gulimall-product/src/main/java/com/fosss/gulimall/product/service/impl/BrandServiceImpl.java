package com.fosss.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fosss.gulimall.product.service.CategoryBrandRelationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.Query;

import com.fosss.gulimall.product.dao.BrandDao;
import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Resource
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new LambdaQueryWrapper<BrandEntity>()
                        .orderByAsc(BrandEntity::getSort)
                        .like(BrandEntity::getBrandId, key)
                        .or().like(BrandEntity::getName, key)
                        .or().like(BrandEntity::getDescript, key)
        );

        return new PageUtils(page);
    }

    /**
     * 修改品牌,同时更新关系表中的数据
     */
    @Override
    @Transactional//加上事务
    public void updateDetails(BrandEntity brand) {
        //更新品牌表
        baseMapper.updateById(brand);
        //如果品牌的名字改变了的话就更新品牌分类关系表
        if (!StringUtils.isEmpty(brand.getName())) {
            categoryBrandRelationService.updateBrand(brand.getBrandId(), brand.getName());
        }
    }

    /**
     * 删除
     */
    @Transactional
    @Override
    public void removeDetails(List<Long> asList) {


        //1.删除品牌
        baseMapper.deleteBatchIds(asList);
        //2.同步删除品牌分类关系表中的数据
        categoryBrandRelationService.deleteBrand(asList);

    }

}













