package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SendTopic {


    @Autowired
    private AmqpTemplate rabbitTemplate;
@Autowired
    private MqQueue mqQueue;
    public void send() {
        String msg1 = "I am topic.mesaage msg======";
        System.out.println("sender1 : " + msg1);
        this.rabbitTemplate.convertAndSend(mqQueue.exchange().getName(), "11", msg1);

        String msg2 = "I am topic.mesaages msg########";
        System.out.println("sender2 : " + msg2);
        this.rabbitTemplate.convertAndSend(mqQueue.exchange().getName(), "topic", msg2);
    }

}
