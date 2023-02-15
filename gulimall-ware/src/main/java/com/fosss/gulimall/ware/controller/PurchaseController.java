package com.fosss.gulimall.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fosss.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fosss.gulimall.ware.entity.PurchaseEntity;
import com.fosss.gulimall.ware.service.PurchaseService;
import com.fosss.common.utils.PageUtils;
import com.fosss.common.utils.R;


/**
 * 采购信息
 *
 * @author fmh
 * @email 1745179058@qq.com
 * @date 2023-01-13 16:38:38
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 领取采购单
     * @param purchaseIds 采购单id
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> purchaseIds){
        purchaseService.received(purchaseIds);
        return R.ok();
    }

    /**
     * 合并采购需求
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo) {
        purchaseService.merge(mergeVo);
        return R.ok();
    }

    /**
     * 查询采购单
     */
    @GetMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.list(params);
        return R.ok().put("page", page);
    }

    /**
     * 合并采购需求
     */
    @RequestMapping("/unreceive/list")
    public R queryPage(@RequestParam Map<String, Object> params) {
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase) {
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
        purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase) {
        purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
