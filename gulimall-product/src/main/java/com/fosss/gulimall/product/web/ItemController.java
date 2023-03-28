package com.fosss.gulimall.product.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: fosss
 * Date: 2023/3/28
 * Time: 20:41
 * Description:商品详情页
 */
@Slf4j
@Controller
public class ItemController {

    /**
     * 查询该商品详情，并跳转到商品详情页
     */
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable Long skuId, Model model) {
        log.info("查询商品详情，商品skuId为：" + skuId);
        model.addAttribute("info", null);
        return "item";
    }
}


























