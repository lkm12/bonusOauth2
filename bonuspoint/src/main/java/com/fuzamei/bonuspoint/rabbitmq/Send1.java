package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class Send1 {

    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private MqQueue mqQueue;

    public void send1() {
        String sendMessage = "send + " + new Date();
        System.out.println("send");
        System.out.println(new Date());
        System.out.println(mqQueue.queueDan().getName());
        amqpTemplate.convertAndSend(mqQueue.queueDan().getName(), sendMessage);

    }
}
