package com.fosss.gulimall.order;

import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class GulimallOrderApplicationTests {
    @Resource
    private RabbitAdmin rabbitAdmin;

    /**
     * 测试rabbitmq
     */
    @Test
    void testRabbitMq() {

    }

}



















