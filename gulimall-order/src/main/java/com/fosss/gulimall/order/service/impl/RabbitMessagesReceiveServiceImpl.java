package com.fosss.gulimall.order.service.impl;

import com.fosss.gulimall.order.entity.OrderEntity;
import com.fosss.gulimall.order.entity.OrderReturnApplyEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

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
    public void receiveMessages2(OrderEntity orderEntity) {
        System.out.println("接收到的消息：" + orderEntity);
    }

    @RabbitHandler
    public void receiveMessages(Message message, OrderReturnApplyEntity orderReturnApplyEntity, Channel channel) {
        System.out.println("接收到的消息：" + orderReturnApplyEntity);
        //deliveryTag是自增的，代表第几个消息
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            if (deliveryTag % 2 == 0) {
                //接收消息
                channel.basicAck(deliveryTag, false);
                System.out.println("接受了消息》》" + deliveryTag);
            } else {
                //拒绝消息
                channel.basicNack(deliveryTag, false, false);
                System.out.println("拒绝了消息》》" + deliveryTag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //@RabbitListener(queues = {"hello-java-queue"})
    //public void receiveMessages(Message message){
    //    System.out.println("接收到的消息："+message);
    //}
}
















