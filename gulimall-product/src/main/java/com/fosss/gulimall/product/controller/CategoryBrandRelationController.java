package com.fosss.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.product.entity.CategoryBrandRelationEntity;
import com.fosss.gulimall.product.service.CategoryBrandRelationService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;


/**
 * 品牌分类关联
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    /**
     * 查询分类关联的品牌
     */
    @GetMapping("/brands/list")
    public R getBrandsByCid(@RequestParam("catId") Long catId) {
        List<BrandEntity> brandEntityList = categoryBrandRelationService.getBrandsByCid(catId);
        //转成vo
        List<BrandVo> list = brandEntityList.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return R.ok().put("data", list);
    }

    /**
     * 列表
     */
    @RequestMapping("/catelog/list")
    public R list(@RequestParam Long brandId) {
        List<CategoryBrandRelationEntity> list = categoryBrandRelationService.list(new LambdaQueryWrapper<CategoryBrandRelationEntity>().eq(CategoryBrandRelationEntity::getBrandId, brandId));

        return R.ok().put("data", list);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.saveDetails(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation) {
        categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
