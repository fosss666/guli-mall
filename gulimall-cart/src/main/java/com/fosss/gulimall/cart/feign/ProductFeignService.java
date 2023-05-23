package com.fosss.gulimall.cart.feign;

import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: fosss
 * Date: 2023/5/23
 * Time: 21:12
 * Description: 远程调用
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    /**
     * 根据skuId获取sku信息
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}




















