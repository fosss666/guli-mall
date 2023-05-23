package com.fosss.gulimall.cart.controller;

import com.fosss.gulimall.cart.interceptor.GulimallInterceptor;
import com.fosss.gulimall.cart.vo.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: fosss
 * Date: 2023/5/21
 * Time: 13:44
 * Description:
 */
@Controller
public class CartController {
    @RequestMapping("/success.html")
    public String success() {
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




















