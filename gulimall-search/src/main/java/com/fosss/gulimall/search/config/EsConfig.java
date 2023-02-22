package com.fosss.gulimall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fosss
 * @date 2023/2/22
 * @description： es配置类
 */
@Configuration
public class EsConfig {
    @Value("${es.config.hostname}")
    private String hostname;
    @Value("${es.config.port}")
    private int port;
    @Value("${es.config.scheme}")
    private String scheme;

    @Bean
    public RestHighLevelClient esClient() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostname, port, scheme)
                        //, new HttpHost("localhost", 9201, "http")
                ));
        return client;
    }

    private static final RequestOptions COMMON_OPTIONS;
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        //builder.addHeader("Authorization", "Bearer " + TOKEN);
        //builder.setHttpAsyncResponseConsumerFactory(
        //        new HttpAsyncResponseConsumerFactory
        //                .HeapBufferedResponseConsumerFactory(30 * 1024 * 1024 * 1024));
        COMMON_OPTIONS = builder.build();
    }

}






















