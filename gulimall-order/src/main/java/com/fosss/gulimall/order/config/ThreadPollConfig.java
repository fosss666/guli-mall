package com.fosss.gulimall.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: fosss
 * Date: 2023/5/31
 * Time: 20:21
 * Description:
 */
@Configuration
public class ThreadPollConfig {
    @Bean
    public java.util.concurrent.ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new java.util.concurrent.ThreadPoolExecutor(
                pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }
}
