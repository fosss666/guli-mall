package com.fosss.gulimall.order.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * @author: fosss
 * Date: 2023/5/31
 * Time: 20:19
 * Description:
 */
@Configuration
public class GulimallSessionConfig {
    //配置session作用域
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
        defaultCookieSerializer.setCookieName("GULISESSION");
        defaultCookieSerializer.setDomainName("localhost");
        return defaultCookieSerializer;
    }

    //配置序列化方式为json
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new GenericFastJsonRedisSerializer();
    }

}
