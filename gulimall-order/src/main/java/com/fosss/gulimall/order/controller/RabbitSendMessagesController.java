package com.fosss.gulimall.order.controller;

import com.fosss.gulimall.order.entity.OrderReturnApplyEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author: fosss
 * Date: 2023/5/29
 * Time: 22:22
 * Description:
 */
@RestController
public class RabbitSendMessagesController {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/sendMsg")
    public String sendMsg(@RequestParam(value = "num", defaultValue = "10") Integer num) {
        OrderReturnApplyEntity entity = new OrderReturnApplyEntity();
        entity.setCreateTime(new Date());
        entity.setId(1L);
        for (int i = 0; i < num; i++) {
            //发送对象类型的消息，对象需要实现序列化接口；若想转为json字符串，需另行配置
            rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", entity);
        }
        return "ok";
    }
}
