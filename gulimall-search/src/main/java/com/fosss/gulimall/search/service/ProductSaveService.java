package com.fosss.gulimall.search.service;

import com.fosss.common.to.es.SkuEsModel;

import java.io.IOException;
import java.util.List;

/**
 * @author fosss
 * @date 2023/2/24
 * @description：
 */
public interface ProductSaveService {    /**
 * 商品上架功能
 */
    boolean productSave(List<SkuEsModel> skuEsModels) throws IOException;
}
