package com.fosss.gulimall.order.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: fosss
 * Date: 2023/5/29
 * Time: 21:41
 * Description:
 */
@Configuration
public class RabbitConfig {
    /**
     * 配置消息转化成json
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
