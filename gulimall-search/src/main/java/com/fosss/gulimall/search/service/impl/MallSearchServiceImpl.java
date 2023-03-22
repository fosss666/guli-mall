package com.fosss.gulimall.search.service.impl;

import com.fosss.gulimall.search.config.EsConfig;
import com.fosss.gulimall.search.constant.EsConstant;
import com.fosss.gulimall.search.service.MallSearchService;
import com.fosss.gulimall.search.vo.SearchParam;
import com.fosss.gulimall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.rescore.RescorerBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author: fosss
 * Date: 2023/3/12
 * Time: 19:45
 * Description:
 */
@Service
public class MallSearchServiceImpl implements MallSearchService {

    private RestHighLevelClient client;

    /**
     * 从es中查询搜索页数据并返回
     */
    @Override
    public SearchResult search(SearchParam searchParam) {
        //构建检索请求
        SearchRequest searchRequest = buildSearchRequest(searchParam);
        //进行检索
        SearchResult result = null;
        try {
            SearchResponse response = client.search(searchRequest, EsConfig.COMMON_OPTIONS);
            //构建返回数据
            result = buildSearchResult(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 构建检索请求
     */
    private SearchRequest buildSearchRequest(SearchParam param) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        /**
         * 模糊匹配，过滤（按照属性，分类，品牌，价格区间，库存）
         */
        //1. 构建bool-query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //1.1 bool-must
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }

        //1.2 bool-fiter
        //1.2.1 catelogId
        if (null != param.getCatalog3Id()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalog3Id()));
        }

        //1.2.2 brandId
        if (null != param.getBrandId() && param.getBrandId().size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("brandId", param.getBrandId()));
        }

        //1.2.3 attrs
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {

            param.getAttrs().forEach(item -> {
                //attrs=1_5寸:8寸&2_16G:8G
                BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();


                //attrs=1_5寸:8寸
                String[] s = item.split("_");
                String attrId = s[0];//属性id
                String[] attrValues = s[1].split(":");//这个属性检索用的值
                boolQuery.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                boolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));

                NestedQueryBuilder nestedQueryBuilder = QueryBuilders.nestedQuery("attrs", boolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQueryBuilder);
            });

        }

        //1.2.4 hasStock
        if (null != param.getHasStock()) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }


        //1.2.5 skuPrice
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            //skuPrice形式为：1_500或_500或500_
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("skuPrice");
            String[] price = param.getSkuPrice().split("_");
            if (price.length == 2) {
                rangeQueryBuilder.gte(price[0]).lte(price[1]);
            } else if (price.length == 1) {
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQueryBuilder.lte(price[1]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    rangeQueryBuilder.gte(price[0]);
                }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);
        }

        //封装所有的查询条件
        searchSourceBuilder.query(boolQueryBuilder);


        /**
         * 排序、高亮、分页
         */
        //排序
        if (!StringUtils.isEmpty(param.getSort())) {
            String[] s = param.getSort().split("_");
            SortOrder order = "asc".equals(s[1]) ? SortOrder.ASC : SortOrder.DESC;
            searchSourceBuilder.sort(s[0], order);
        }
        //高亮
        if (!StringUtils.isEmpty(param.getKeyword())) {
            HighlightBuilder builder = new HighlightBuilder();
            builder.field("sukTitle");
            builder.preTags("<b style='color:red'>");
            builder.postTags("<b/>");
            searchSourceBuilder.highlighter(builder);
        }
        //分页
        searchSourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        searchSourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);

        /**
         * 聚合分析
         */


        //构建检索请求
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, searchSourceBuilder);
        return searchRequest;
    }

    /**
     * 构建返回数据
     */
    private SearchResult buildSearchResult(SearchResponse response) {
        return null;
    }
}






















