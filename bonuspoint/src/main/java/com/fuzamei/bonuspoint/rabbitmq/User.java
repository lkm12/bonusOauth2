package com.fuzamei.bonuspoint.rabbitmq;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
public class User implements Serializable {
    private String name;
    private Integer age;


}
