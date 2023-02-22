package com.fosss.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.fosss.gulimall.search.config.EsConfig;
import lombok.Data;
import lombok.ToString;
import org.bouncycastle.est.ESTClient;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallSearchApplicationTests {

    @Resource
    //private EsConfig esConfig;
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        //System.out.println(esConfig.esClient());
        System.out.println(client);
    }

    @Data
    @ToString
    static class Account {
        private int account_number;

        private int balance;

        private String firstname;

        private String lastname;

        private int age;

        private String gender;

        private String address;

        private String employer;

        private String email;

        private String city;

        private String state;

    }

    /**
     * 测试复杂检索-搜索 address 中包含 mill 的所有人的年龄分布以及平均年龄，但不显示这些人的详情
     * GET bank/_search
     * {
     * "query": {
     * "match": {
     * "address": "mill"
     * }
     * },
     * "aggs": {
     * "ageAggs": {
     * "terms": {
     * "field": "age",
     * "size": 10
     * }
     * },
     * "ageAvg": {
     * "avg": {
     * "field": "age"
     * }
     * }
     * },
     * "size": 0
     * }
     */
    @Test
    public void testSearch() throws IOException {
        //构建检索请求
        SearchRequest searchRequest = new SearchRequest("newbank");
        //构建检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        //构建聚合条件
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAggs").field("age").size(10);
        AvgAggregationBuilder ageAvg = AggregationBuilders.avg("ageAvgs").field("age");
        searchSourceBuilder.aggregation(ageAgg);
        searchSourceBuilder.aggregation(ageAvg);

        //进行检索
        searchRequest.source(searchSourceBuilder);
        System.out.println("检索条件：" + searchRequest);
        SearchResponse response = client.search(searchRequest, EsConfig.COMMON_OPTIONS);
        System.out.println("检索结果：" + response);

        //解析返回值
        SearchHits hits = response.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            String sourceAsString = searchHit.getSourceAsString();
            //转为对象
            Account account = JSON.parseObject(sourceAsString, Account.class);
            System.out.println("account = " + account);
        }
    }

    /**
     * 测试根据id查询数据
     */
    @Test
    public void testGet() throws IOException {
        GetRequest getRequest = new GetRequest("users");
        getRequest.id("1");
        GetResponse response = client.get(getRequest, EsConfig.COMMON_OPTIONS);
        System.out.println("response = " + response);
    }

    /**
     * 测试保存数据
     */
    @Test
    public void testIndexData() throws IOException {
        User user = new User();
        user.setUsername("张三");
        user.setAge(18);
        user.setGender('难');
        //转为json字符串
        String jsonString = JSON.toJSONString(user);
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");//不传的话默认生成
        indexRequest.source(jsonString, XContentType.JSON);//传键值对或json字符串
        //执行操作
        IndexResponse response = client.index(indexRequest, EsConfig.COMMON_OPTIONS);
        System.out.println("response = " + response);
    }

    @Data
    class User {
        private String username;
        private Integer age;
        private Character gender;
    }

}


















