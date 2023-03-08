package com.fosss.gulimall.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;

import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;

/**
 * @author: fosss
 * Date: 2023/3/8
 * Time: 16:32
 * Description:
 */
@Controller
public class SearchController {
    @GetMapping(value = "/search.html")
    public String listPage() {

        return "search";
    }
}
