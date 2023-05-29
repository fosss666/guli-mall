package com.fosss.gulimall.order.service.impl;

import com.fosss.gulimall.order.entity.OrderEntity;
import com.fosss.gulimall.order.entity.OrderReturnApplyEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * @author: fosss
 * Date: 2023/5/29
 * Time: 22:16
 * Description: 接受发送的消息
 */
@RabbitListener(queues = {"hello-java-queue"})
@Service
public class RabbitMessagesReceiveServiceImpl {

    @RabbitHandler
    public void receiveMessages2(OrderEntity message){
        System.out.println("接收到的消息："+message);
    }

    @RabbitHandler
    public void receiveMessages(OrderReturnApplyEntity message){
        System.out.println("接收到的消息："+message);
    }

    //@RabbitListener(queues = {"hello-java-queue"})
    //public void receiveMessages(Message message){
    //    System.out.println("接收到的消息："+message);
    //}
}
















