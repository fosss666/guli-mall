package com.fosss.gulimall.product.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author: fosss
 * Date: 2023/5/12
 * Time: 20:50
 * Description:配置session的序列化和作用域
 */

@Configuration
public class GulimallSessionConfig {
    //配置session作用域
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("GULISESSION");
        serializer.setCookiePath("/");
        // 设置cookie的作用域为父域名
        serializer.setDomainName("localhost");
        return serializer;
    }

    //配置序列化方式为json  TODO 注意方法名一定要是springSessionDefaultRedisSerializer，否则就不会生效（为什么）RedisHttpSessionConfiguration 中有这么一个方法，标注了@Qualifier(“springSessionDefaultRedisSerializer”)，这就是为什么RedisSerializer的name要命名为"springSessionDefaultRedisSerializer"的原因
    @Bean
    public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }
}
