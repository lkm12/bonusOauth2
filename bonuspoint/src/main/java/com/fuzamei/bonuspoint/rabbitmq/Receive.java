package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "sendd")
public class Receive {

    @RabbitHandler
    public void process(User send) {
        System.out.println("receive:" + send);
    }

}
