package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "sendd")
public class Receive1 {

    @RabbitHandler
    public void proce(User ss){
        System.out.println("receive1" + ss);
    }
}
