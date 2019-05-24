package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "message")
public class ReceiveTopic1 {
    @RabbitHandler
    public void receive(String ss) {
        System.out.println("receiveTopic" + ss);
    }
}
