package org.pdaodao.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.pdaodao.config.RabbitConfig.EXCHANGE_NORMAL;


@RestController
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/test2/{msg}")
    public void test(@PathVariable String msg) {
        rabbitTemplate.convertAndSend(EXCHANGE_NORMAL, "key1", msg);
        rabbitTemplate.convertAndSend(EXCHANGE_NORMAL, "key2", msg);
    }
}
