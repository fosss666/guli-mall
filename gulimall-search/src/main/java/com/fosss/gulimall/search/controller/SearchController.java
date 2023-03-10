package com.fosss.gulimall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author: fosss
 * Date: 2023/3/8
 * Time: 16:32
 * Description:
 */
@Controller
public class SearchController {
    @GetMapping(value = "/list.html")
    public String listPage() {

        return "list";
    }
}
