package com.fosss.gulimall.product.web;

import com.fosss.gulimall.product.service.SkuInfoService;
import com.fosss.gulimall.product.service.SpuInfoService;
import com.fosss.gulimall.product.vo.SkuItemVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

/**
 * @author: fosss
 * Date: 2023/3/28
 * Time: 20:41
 * Description:商品详情页
 */
@Slf4j
@Controller
public class ItemController {

    @Resource
    private SkuInfoService skuInfoService;

    /**
     * 查询该商品详情，并跳转到商品详情页
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable Long skuId, Model model) {
        log.info("查询商品详情，商品skuId为：" + skuId);
        SkuItemVo item = skuInfoService.item(skuId);
        model.addAttribute("info", item);
        return "item";
    }
}


























