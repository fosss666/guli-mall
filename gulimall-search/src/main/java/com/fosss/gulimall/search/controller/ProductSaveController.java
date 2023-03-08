package com.fosss.gulimall.search.controller;

import com.fosss.common.exception.ExceptionResult;
import com.fosss.common.to.es.SkuEsModel;
import com.fosss.common.utils.R;
import com.fosss.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author fosss
 * @date 2023/2/24
 * @description：
 */
@Slf4j
@RestController
@RequestMapping("/elasticSearch")
public class ProductSaveController {

    @Resource
    private ProductSaveService productSaveService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    /**
     * 商品上架功能
     */
    @PostMapping("/productSave")
    public R productSave(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean res = false;
        try {
            res = productSaveService.productSave(skuEsModels);
        } catch (IOException e) {
            //出现异常
            log.error("商品上架失败");
            return R.error(ExceptionResult.ES_SAVE_EXCEPTION.getMessage());
        }
        if (res) {
            return R.ok();
        } else {
            return R.error(ExceptionResult.ES_SAVE_EXCEPTION.getMessage());
        }
    }
}




















