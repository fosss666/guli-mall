package com.fosss.gulimall.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * 读多写少读数据直接用SpringCache,写数据可以加读写锁，读多写多直接查数据库
 * 常规数据（读多写少，即时性、一致性要求不高的数据）完全可以使用springCache的注解来使用缓存   写模式只要有缓存过期时间就足够了
 * 其他数据：特殊处理   比如高并发的用redisson
 * <p>
 * 配置spring-cache
 *
 * @Cacheable: Triggers cache population.触发器缓存填充
 * @CacheEvict: Triggers cache eviction.触发缓存逐出
 * @CachePut: Updates the cache without interfering with the method execution.在不干扰方法执行的情况下更新缓存
 * @Caching: Regroups multiple cache operations to be applied on a method.组合以上多个缓存操作
 * @CacheConfig: Shares some common cache-related settings at class-level.在类级别共享一些与缓存相关的常见设置
 * <p>
 * 整合springCache:
 * 1.导入坐标spring-boot-starter-cache
 * 2.配置缓存类型为redis
 * 3.开启缓存注解 @EnableCaching
 * 4.在需要缓存的方法上添加相应注解
 */
@EnableRedisHttpSession //开启使用redis存储session
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
