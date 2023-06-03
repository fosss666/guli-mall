package com.fosss.gulimall.order.feign;

import com.fosss.gulimall.order.vo.OrderItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author: fosss
 * Date: 2023/6/3
 * Time: 21:59
 * Description:
 */
@FeignClient("gulimall-cart")
public interface CartFeignService {
    /**
     * 远程获取购物车中被选中的数据
     */
    @GetMapping("/checkedData")
    public List<OrderItemVo> getCheckedCart();
}
