package com.fosss.gulimall.cart.config;

import com.fosss.gulimall.cart.interceptor.GulimallInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: fosss
 * Date: 2023/5/22
 * Time: 21:43
 * Description:
 */
@Configuration
public class GulimallWebConfig implements WebMvcConfigurer {
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GulimallInterceptor()).addPathPatterns("/**");
    }
}
