package com.fosss.gulimall.order.web;

import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: fosss
 * Date: 2023/6/2
 * Time: 22:51
 * Description:
 */
@Controller
public class OrderWebController {

    @GetMapping("/toTrade")
    public String toTrade() {
        return "confirm";
    }
}
