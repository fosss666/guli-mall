package com.fosss.gulimall.product.feign;

import com.fosss.common.to.SpuBoundTo;
import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author fosss
 * @date 2023/2/7
 * @description：
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    //保存积分信息
    @PostMapping("/coupon/spubounds/save")
    R save(@RequestBody SpuBoundTo spuBoundTo);
}

















