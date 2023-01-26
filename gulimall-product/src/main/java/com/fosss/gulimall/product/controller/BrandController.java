package com.fosss.gulimall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fosss.common.validGroup.AddGroup;
import com.fosss.common.validGroup.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.product.entity.BrandEntity;
import com.fosss.gulimall.product.service.BrandService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 测试远程被调用的测试接口
     */
    @RequestMapping("/test/getBrands")
    public R testGetBrands() {
        List<BrandEntity> brands = brandService.list();
        return R.ok().put("brands", brands);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //Valid JSR303数据校验
    public R save(/*@Valid*/ @Validated(AddGroup.class) @RequestBody BrandEntity brand/*, BindingResult result*/) {
        //if (result.hasErrors()) {
        //    HashMap<String, String> map = new HashMap<>();
        //    result.getFieldErrors().forEach((item) -> {
        //        String field = item.getField();
        //        String message = item.getDefaultMessage();
        //        map.put(field, message);
        //    });
        //    return R.error(400, "表单内容有误").put("data", map);
        //}
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
