package org.pdaodao.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static org.pdaodao.config.RabbitConfig.*;


@RestController
public class TestController {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * springboot整合rabbitmq
     * 先设置 交换机
     * 在设置 队列，同时设置队列关联的死信交换机
     * 在设置 绑定
     * 在设置 监听那个队列被消费者消费
     *
     * 注意 如果TTL时间是设置在队列上和
     *     单独给每个消息设置过期时间效果不同
     */
    @RequestMapping("/test/{msg}")
    public void test(@PathVariable("msg") String msg) {
        rabbitTemplate.convertAndSend(EXCHANGE_X, ROUTE_A, "消息来自 ttl 为 10S 的队列: " + msg);
        rabbitTemplate.convertAndSend(EXCHANGE_X, ROUTE_B, "消息来自 ttl 为 1S 的队列: " + msg);
    }
}
