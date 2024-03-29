package org.pdaodao.demo6;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer1 {

    public static final String EXCHANGE_NAME = "log_direct";

    /**
     * 1. 创建channel，声明队列
     * 2。 queueBind 队列和交换机绑定
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "blue");

        channel.basicConsume(queue, true, (s, delivery) -> {
            String res = new String(delivery.getBody());

            System.out.println(res);
        }, s -> {
            System.out.println("CancelCallback");
        });

    }
}
