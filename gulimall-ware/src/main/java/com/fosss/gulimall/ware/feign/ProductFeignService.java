package com.fosss.gulimall.ware.feign;

import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fosss
 * @date 2023/2/17
 * @description： 远程调用商品服务接口
 * 可以调用gulimall-product，url为/product/skuinfo/info/{skuId}
 * 也可以调用网关gulimall-gateway,url为/api/product/skuinfo/info/{skuId}
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * 查询skuName
     * @param skuId
     * @return map.get("skuInfo")
     */
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}

















