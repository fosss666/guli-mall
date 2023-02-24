package com.fosss.gulimall.product.web;

import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author fosss
 * @date 2023/2/24
 * @description： 前台首页页面
 */
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;

    @GetMapping({"/","/index.html"})
    public String indexPage(Model model) {
        //查询一级分类
        List<CategoryEntity> categories = categoryService.getLevel1();
        //转发到thymeleaf页面中
        model.addAttribute("categories", categories);
        //默认前缀：classpath:/templates/  后缀：.html
        return "index";
    }
}





















