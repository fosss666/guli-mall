package com.fosss.gulimall.cart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: fosss
 * Date: 2023/5/21
 * Time: 13:44
 * Description:
 */
@Controller
public class IndexController {
    @RequestMapping("/success.html")
    public String success() {
        return "success";
    }

    @RequestMapping("/cartList.html")
    public String cartList() {
        return "cartList";
    }
}
