package com.fosss.gulimall.coupon.feign;

import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fosss
 * date 2023/1/13
 * description：
 * 用于远程调用接口
 */
@FeignClient("gulimall-product")//指定被调用的是哪个服务
public interface BrandFeignService {
    //注意路径要写全路径
    @RequestMapping("/product/brand/test/getBrands")
    public R testGetBrands();
}















