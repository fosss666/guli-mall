package com.fosss.gulimall.cart.feign;

import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/5/23
 * Time: 21:12
 * Description: 远程调用
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    /**
     * 根据skuId查询pms_sku_sale_attr_value表中的信息
     */
    @GetMapping("/product/skusaleattrvalue/stringList/{skuId}")
    List<String> getAttrsAsStringList(@PathVariable("skuId") Long skuId);
    /**
     * 根据skuId获取sku信息
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}




















