package com.fosss.gulimall.order;

import com.fosss.gulimall.order.entity.OrderReturnApplyEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallOrderApplicationTests {
    @Autowired
    private AmqpAdmin amqpAdmin;
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试创建交换机
     */
    @Test
    public void testExchange() {
        //直接型交换机 durable-是否持久化
        DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
        amqpAdmin.declareExchange(directExchange);
        log.info("Exchange[{}]创建成功", "hello-java-exchange");
    }

    /**
     * 测试创建队列
     */
    @Test
    public void testQueue() {
        //exclusive-是否排他
        Queue queue = new Queue("hello-java-queue", true, false, false);
        amqpAdmin.declareQueue(queue);
        log.info("Queue[{}]创建成功", "hello-java-queue");
    }

    /**
     * 测试绑定关系
     */
    @Test
    public void testBinding() {
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE, "hello-java-exchange", "hello.java", new HashMap<>());
        amqpAdmin.declareBinding(binding);
        log.info("Binding[{}]创建成功", "hello-java-binding");
    }

    /**
     * 测试发消息
     */
    @Test
    public void testSendMessage() {
        OrderReturnApplyEntity entity = new OrderReturnApplyEntity();
        entity.setCreateTime(new Date());
        entity.setId(1L);
        //发送对象类型的消息，对象需要实现序列化接口；若想转为json字符串，需另行配置
        rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", entity);
        log.info("message发送成功：{}", entity);
    }

}



















