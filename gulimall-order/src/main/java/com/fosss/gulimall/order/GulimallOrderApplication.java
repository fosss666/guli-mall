package com.fosss.gulimall.order;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * 整合rabbitmq:
 * 1.引入amqp环境坐标
 * 2.配置rabbitmq环境
 * 3.开启对rabbitmq的支持 @EnableRabbit
 * 4.使用RabbitAdmin创建exchange、queue等，进行收发消息等操作
 */

@EnableRabbit
@EnableDiscoveryClient//开启注册中心
@SpringBootApplication
@ComponentScan(basePackages = "com.fosss")
public class GulimallOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallOrderApplication.class, args);
    }

}
