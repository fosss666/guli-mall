package com.fosss.gulimall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.fosss.gulimall.product.vo.AttrRespVo;
import com.fosss.gulimall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fosss.gulimall.product.entity.AttrEntity;
import com.fosss.gulimall.product.service.AttrService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;


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

    /**
     * 列表
     */
    @RequestMapping("/base/list/{catelogId}")
    public R list(@PathVariable("catelogId") Long catelogId, @RequestParam Map<String, Object> params) {
        PageUtils page = attrService.queryPage(catelogId, params);

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
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr) {
        attrService.updateById(attr);

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
