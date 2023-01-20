package com.fosss.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;


/**
 * 商品三级分类
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 更新拖拽后的分类排序，父id等
     */
    @PutMapping("/update/drag")
    public R updateDrag(@RequestBody CategoryEntity[] categories) {
        if (categories != null && categories.length > 0) {
            categoryService.updateBatchById(Arrays.asList(categories));
        }
        return R.ok();
    }

    /**
     * 查询所有的分类，并组装成树形(父子)结构返回
     */
    @RequestMapping("/list/tree")
    public R list() {
        List<CategoryEntity> data = categoryService.listTree();
        return R.ok().put("data", data);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return R.ok();
    }

    /**
     * 逻辑删除
     */
    @PostMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        categoryService.removeMenu(Arrays.asList(catIds));

        return R.ok();
    }

}
