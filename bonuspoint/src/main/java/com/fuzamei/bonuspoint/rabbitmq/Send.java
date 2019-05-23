package com.fuzamei.bonuspoint.rabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Send {

    private AmqpTemplate amqpTemplate;
    private MqQueue mqQueue;
    private User user;

    @Autowired
    public Send(AmqpTemplate amqpTemplate,MqQueue mqQueue,User user){
        this.amqpTemplate = amqpTemplate;
        this.mqQueue = mqQueue;
        this.user = user;
    }




   public void send(){
       User user = new User();
       user.setAge(10);
       user.setName("kuoming");

       String sendMessage = "send + "+ user;

       System.out.println("send");

       System.out.println(mqQueue.queueDan().getName());
       amqpTemplate.convertAndSend(mqQueue.queueDan().getName(),user);

   }
}
