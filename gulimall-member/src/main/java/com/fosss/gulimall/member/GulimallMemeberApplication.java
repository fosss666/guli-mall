package com.fosss.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient//开启注册中心
@SpringBootApplication
public class GulimallMemeberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMemeberApplication.class, args);
    }

}
