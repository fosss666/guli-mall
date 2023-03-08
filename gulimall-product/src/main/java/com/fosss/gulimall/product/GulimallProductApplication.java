package com.fosss.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * 配置spring-cache
 *
 * @Cacheable: Triggers cache population.触发器缓存填充
 *
 * @CacheEvict: Triggers cache eviction.触发缓存逐出
 *
 * @CachePut: Updates the cache without interfering with the method execution.在不干扰方法执行的情况下更新缓存
 *
 * @Caching: Regroups multiple cache operations to be applied on a method.组合以上多个缓存操作
 *
 * @CacheConfig: Shares some common cache-related settings at class-level.在类级别共享一些与缓存相关的常见设置
 *
 * 整合springCache:
 * 1.导入坐标spring-boot-starter-cache
 * 2.配置缓存类型为redis
 * 3.开启缓存注解 @EnableCaching
 * 4.在需要缓存的方法上添加相应注解
 */

@EnableCaching //开启缓存
@EnableDiscoveryClient//开启注册中心
@SpringBootApplication
@ComponentScan(basePackages = "com.fosss")
//开启远程调用功能
@EnableFeignClients(basePackages = "com.fosss.gulimall.product.feign")
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
