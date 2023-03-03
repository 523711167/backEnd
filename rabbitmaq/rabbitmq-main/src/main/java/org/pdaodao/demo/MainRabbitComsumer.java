package org.pdaodao.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainRabbitComsumer {

    private final static String QUEUE_NAME = "hello";

    /**
     * 简单模式
     * 1. 通过工厂 ConnectionFactory 生成 Connection
     * 2. 创建channel，channel.basicConsume消费队列
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.basicConsume(QUEUE_NAME, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.println(res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });

    }
}
