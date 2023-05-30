package com.fosss.gulimall.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author: fosss
 * Date: 2023/5/29
 * Time: 21:41
 * Description:
 */
@Configuration
public class RabbitConfig {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 定制rabbitTemplate实现可靠投递
     * 1.发送端确认
     * 1）消息传送到代理的回调
     * ①在配置中开启
     * ②进行设置
     * 2）消息正确到达队列的回调
     * ①在配置中开启    publisher-returns: true   template:mandatory: true
     * ②进行设置
     * <p>
     * 2.消费端确认
     *
     */
    @PostConstruct //在当前配置类构造完成后执行下面的方法
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 消息的唯一标识（发送时携带的id）
             * @param ack 消息是否回复成功
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("correlationData = " + correlationData);
                System.out.println("ack = " + ack);
                System.out.println("cause = " + cause);
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 消息传递到队列失败时调用此方法
             * @param message 消息的内容
             * @param replyCode 返回状态码
             * @param replyText 返回的文本内容-失败原因
             * @param exchange 哪个交换机
             * @param routingKey 哪个路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.out.println("message = " + message);
                System.out.println("replyCode = " + replyCode);
                System.out.println("replyText = " + replyText);
                System.out.println("exchange = " + exchange);
                System.out.println("routingKey = " + routingKey);
            }
        });
    }

    /**
     * 配置消息转化成json
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}




















