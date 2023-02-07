package com.fosss.gulimall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.fosss.common.to.SkuReductionTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.coupon.entity.SkuFullReductionEntity;
import com.fosss.gulimall.coupon.service.SkuFullReductionService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;


/**
 * 商品满减信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:21:28
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存优惠满减等信息
     */
    @PostMapping("/saveReduction")
    R saveReduction(@RequestBody SkuReductionTo skuReductionTo) {
        skuFullReductionService.saveReduction(skuReductionTo);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction) {
        skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
