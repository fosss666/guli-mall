package com.fosss.gulimall.product.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author fosss
 * @date 2023/2/23
 * @description： 远程调用库存服务
 */
@FeignClient("gulimall-ware")
public interface WareFeignService {
    /**
     * 根据skuId查询是否有库存  库存=现有库存-锁定的库存
     */
    @PostMapping("/ware/waresku/hasStock")
    Map<Long, Boolean> hasStock(@RequestBody List<Long> skuIds);
}
