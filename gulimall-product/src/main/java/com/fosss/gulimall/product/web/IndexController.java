package com.fosss.gulimall.product.web;

import com.fosss.common.constant.RedisConstant;
import com.fosss.gulimall.product.entity.CategoryEntity;
import com.fosss.gulimall.product.service.CategoryService;
import com.fosss.gulimall.product.vo.Catelog2Vo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author fosss
 * @date 2023/2/24
 * @description： 前台首页页面
 */
@Slf4j
@Controller
public class IndexController {
    @Resource
    private CategoryService categoryService;
    @Resource
    private RedissonClient redissonClient;

    /**
     * 测试redisson分布式锁
     */
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        RLock lock = redissonClient.getLock(RedisConstant.REDISSON_LOCK_KEY);
        try {
            lock.lock();
            log.info("上锁" + Thread.currentThread().getId());
            Thread.sleep(10000);
        } catch (Exception e) {
        } finally {
            lock.unlock();
            log.info("解锁");
        }
        return "hello world";
    }

    /**
     * 获取所有一级分类
     */
    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //查询一级分类
        List<CategoryEntity> categories = categoryService.getLevel1();
        //转发到thymeleaf页面中
        model.addAttribute("categories", categories);
        //默认前缀：classpath:/templates/  后缀：.html
        return "index";
    }

    /**
     * 获取二三级分类
     */
    @ResponseBody
    @GetMapping("/index/catelog.json")
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        return categoryService.getCatelogJson();
    }
}





















