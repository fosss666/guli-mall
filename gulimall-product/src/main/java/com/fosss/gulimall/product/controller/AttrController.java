package com.fosss.gulimall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.fosss.gulimall.product.entity.ProductAttrValueEntity;
import com.fosss.gulimall.product.service.ProductAttrValueService;
import com.fosss.gulimall.product.vo.AttrRespVo;
import com.fosss.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.service.AttrService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;

import javax.annotation.Resource;


/**
 * 商品属性
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-12 17:50:29
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Resource
    private ProductAttrValueService productAttrValueService;

    /**
     * 更新spu规格
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttrValue(@PathVariable("spuId") Long spuId, @RequestBody List<ProductAttrValueEntity> productAttrValueEntities) {
        productAttrValueService.updateSpuAttrValue(spuId, productAttrValueEntities);
        return R.ok();
    }

    /**
     * 获取spu规格
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R getSpuAttrValue(@PathVariable("spuId") Long spuId) {
        List<ProductAttrValueEntity> data = productAttrValueService.getSpuAttrValue(spuId);
        return R.ok().put("data", data);
    }

    /**
     * 列表
     */
    @RequestMapping("/{type}/list/{catelogId}")
    public R list(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params, @PathVariable("type") String type) {
        PageUtils page = attrService.queryPage(catelogId, params, type);

        return R.ok().put("page", page);
    }


    /**
     * 查询属性详情
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId) {
        AttrRespVo attr = attrService.getInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存属性
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr) {
        attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 修改属性详情
     */
    @PostMapping("/update")
    public R update(@RequestBody AttrVo attr) {
        attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds) {
        attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
