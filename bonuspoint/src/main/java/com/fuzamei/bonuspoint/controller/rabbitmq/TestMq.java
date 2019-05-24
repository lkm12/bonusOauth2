package com.fuzamei.bonuspoint.controller.rabbitmq;

import com.fuzamei.bonuspoint.rabbitmq.Send;
import com.fuzamei.bonuspoint.rabbitmq.Send1;
import com.fuzamei.bonuspoint.rabbitmq.SendTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rabbitmq")
public class TestMq {
    private Send send;
    private Send1 send1;
    private SendTopic sendTopic;

    @Autowired
    public TestMq(Send send, Send1 send1, SendTopic sendTopic) {
        this.send = send;
        this.send1 = send1;
        this.sendTopic = sendTopic;
    }


    @PostMapping("/send")
    public void sendTest() {
        send.send();

    }

    @PostMapping("/sendOneToMany")
    public void sendOneToMany() {
        for (int i = 0; i < 5; i++) {
            send.send();
        }
    }

    @PostMapping("/sendManyToMany")
    public void sendManyToMany() {
        for (int i = 0; i < 5; i++) {
            send.send();
            //send1.send1();
        }
//        for (int i = 0;i < 5;i++) {
//            //send.send();
//            send1.send1();
//        }
    }

    @PostMapping("/sendTopic")
    public void sendTopic() {
        sendTopic.send();
    }
}
