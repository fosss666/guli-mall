package com.fosss.gulimall.search.controller;

import com.fosss.gulimall.search.service.MallSearchService;
import com.fosss.gulimall.search.vo.SearchParam;
import com.fosss.gulimall.search.vo.SearchResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

/**
 * @author: fosss
 * Date: 2023/3/8
 * Time: 16:32
 * Description:
 */
@Controller
public class SearchController {
    @Resource
    private MallSearchService mallSearchService;

    /**
     * 从es中查询搜索页数据并返回
     */
    @GetMapping(value = "/list.html")
    public String listPage(SearchParam searchParam, Model model) {
        SearchResult result = mallSearchService.search(searchParam);
        model.addAttribute("result", result);
        return "list";
    }
}





























