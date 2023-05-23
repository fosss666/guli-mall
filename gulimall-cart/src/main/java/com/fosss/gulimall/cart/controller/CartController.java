package com.fosss.gulimall.cart.controller;

import com.fosss.gulimall.cart.interceptor.GulimallInterceptor;
import com.fosss.gulimall.cart.service.CartService;
import com.fosss.gulimall.cart.vo.CartItemVo;
import com.fosss.gulimall.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @author: fosss
 * Date: 2023/5/21
 * Time: 13:44
 * Description:
 */
@Controller
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * 将商品添加到购物车
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId,
                            @RequestParam("num") Integer num,
                            Model model) {
        CartItemVo cartItemVo = cartService.addToCart(skuId, num);
        model.addAttribute("item", cartItemVo);
        return "success";
    }

    /**
     * 浏览器有一个cookie:user-key 标识用户的身份，一个月过期
     * 如果第一次使用jd的购物车功能，都会给一个临时的用户身份:
     * 浏览器以后保存，每次访问都会带上这个cookie；
     * <p>
     * 登录：session有
     * 没登录：按照cookie里面带来user-key来做
     * 第一次，如果没有临时用户，自动创建一个临时用户
     */
    @RequestMapping("/cart.html")
    public String cartList() {
        //从threadLocal中获取用户信息
        UserInfoTo userInfoTo = GulimallInterceptor.threadLocal.get();
        System.out.println(userInfoTo);

        return "cartList";
    }
}



















