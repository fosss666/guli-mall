package com.fosss.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.fosss.common.to.es.SkuEsModel;
import com.fosss.gulimall.search.config.EsConfig;
import com.fosss.gulimall.search.constant.EsConstant;
import com.fosss.gulimall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @author fosss
 * @date 2023/2/24
 * @description：
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Resource
    private RestHighLevelClient client;

    /**
     * 商品上架功能
     */
    @Override
    public boolean productSave(List<SkuEsModel> skuEsModels) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel model : skuEsModels) {
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            indexRequest.id(model.getSkuId().toString());
            indexRequest.source(JSON.toJSONString(model), XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse response = client.bulk(bulkRequest, EsConfig.COMMON_OPTIONS);
        boolean res = response.hasFailures();
        if (res) {
            for (BulkItemResponse item : response.getItems()) {
                log.error("商品上架失败：{}", item.getId());
            }
        }
        //返回是否上架成功，没有错误说明成功
        return !res;
    }
}




















