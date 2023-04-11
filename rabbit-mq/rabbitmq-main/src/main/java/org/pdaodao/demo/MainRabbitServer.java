package org.pdaodao.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.pdaodao.U;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class MainRabbitServer {

    private final static String QUEUE_NAME = "hello";

    /**
     * 简单模式
     * 1. 通过工厂 ConnectionFactory 生成 Connection
     * 2. 创建channel，再声明一个队列channel.queueDeclare
     * 3. 通过channel发布内容channel.basicPublish
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = U.createCon();
        Channel channel = connection.createChannel();
        // exclusive true 只有当前信道可以消费队列
        // durable 是否持久化
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);

        String str = "我是你爹";
        channel.basicPublish("", QUEUE_NAME, null, str.getBytes(StandardCharsets.UTF_8));

    }

}
