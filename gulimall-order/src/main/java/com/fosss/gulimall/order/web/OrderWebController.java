package com.fosss.gulimall.order.web;

import com.fosss.gulimall.order.service.OrderService;
import com.fosss.gulimall.order.vo.OrderConfirmVo;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author: fosss
 * Date: 2023/6/2
 * Time: 22:51
 * Description:
 */
@Controller
public class OrderWebController {

    @Resource
    private OrderService orderService;

    /**
     * 跳转到订单确认页，并将订单确认模型封装好
     */
    @GetMapping("/toTrade")
    public String toTrade() {
        //封装订单确认模型
        OrderConfirmVo orderConfirmData = orderService.getOrderConfirmData();
        return "confirm";
    }
}
