package com.fosss.gulimall.order.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: fosss
 * Date: 2023/6/4
 * Time: 20:07
 * Description:远程调用服务的拦截器，因为feign远程调用会导致请求头消失，从而order服务不能识别到用户登录状态，所以在远程调用前通过这个拦截器将请求头封装
 */
@Configuration
public class OrderWebConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                //获取请求中的Cookie
                if (requestAttributes != null) {
                    HttpServletRequest request = requestAttributes.getRequest();
                    String cookie = request.getHeader("Cookie");
                    template.header("Cookie", cookie);
                }
            }
        };
    }
}
