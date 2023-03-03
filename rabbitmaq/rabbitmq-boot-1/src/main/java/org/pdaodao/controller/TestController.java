package org.pdaodao.controller;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.bootrabbitdemo2.config.RabbitConfig.CONFIRM_EXCHANGE_NAME;

@RestController
public class TestController {


    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * rabbitMq的消息发布确认机制
     * 1。 spring.rabbitmq.publisher-confirm-type=correlated
     * 2。 发送消息时候，消息必须带有一个唯一的ID，机制是这样的
     * 3。 实现RabbitTemplate.ConfirmCallback接口，消息发布确认的回调用接口
     * 4。 通过获取CorrelationData的ID，知道消息是否被已经发送到交换机
     *
     *  ConfirmCallback
     *      消息不管是否到达都会有一个回调函数，通过ack确认
     *  ReturnsCallback
     *      消息route不到队列的时候触发
     */
    @RequestMapping("test/{msg}")
    public void test(@PathVariable String msg) {
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("1");
        String routingKey="key1";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, msg, correlationData1);

        //指定消息 id 为 1
        CorrelationData correlationData2 = new CorrelationData("2");
        String routingKey2 = "key2";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey2, msg, correlationData2);
    }

    @RequestMapping("test1/{msg}")
    public void test1(@PathVariable String msg) {
        //指定消息 id 为 1
        CorrelationData correlationData1 = new CorrelationData("3");
        String routingKey="key1";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey, msg, correlationData1);

        //指定消息 id 为 1
        CorrelationData correlationData2 = new CorrelationData("4");
        String routingKey2 = "key2";

        rabbitTemplate.convertAndSend(CONFIRM_EXCHANGE_NAME, routingKey2, msg, correlationData2);
    }
}
