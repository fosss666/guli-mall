package com.fosss.gulimall.order.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: fosss
 * Date: 2023/5/30
 * Time: 20:31
 * Description:
 */
@Controller
public class HelloController {

    @GetMapping("/{page}.html")
    public String toPage(@PathVariable("page") String page) {
        return page;
    }
}































