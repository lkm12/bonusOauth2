package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "55")
public class ReceiveTopic2 {
    @RabbitHandler
    public void receive(String ss){
        System.out.println("ReceiveTopic2" + ss);
    }
}
