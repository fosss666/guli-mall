package com.fosss.gulimall.product.feign;

import com.fosss.common.to.es.SkuEsModel;
import com.fosss.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author fosss
 * @date 2023/2/24
 * @description： 全文检索功能
 */
@FeignClient("gulimall-search")
public interface SearchFeignService {

    /**
     * 商品上架功能
     */
    @PostMapping("/elasticSearch/productSave")
    R productSave(@RequestBody List<SkuEsModel> skuEsModels);


}














