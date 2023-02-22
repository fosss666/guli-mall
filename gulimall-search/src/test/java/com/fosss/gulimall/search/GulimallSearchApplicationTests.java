package com.fosss.gulimall.search;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
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

    /**
     * 测试根据id查询数据
     */
    @Test
    public void testGet() throws IOException {
        GetRequest getRequest = new GetRequest("users");
        getRequest.id("1");
        GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);
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
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println("response = " + response);
    }

    @Data
    class User {
        private String username;
        private Integer age;
        private Character gender;
    }

}


















