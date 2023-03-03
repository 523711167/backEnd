package org.pdaodao.demo7;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer1 {

    public static final String EXCHANGE_TOPIC = "log_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_TOPIC, "*.*.rabbit");
        channel.queueBind(queue, EXCHANGE_TOPIC, "lazy.#");

        channel.basicConsume(queue, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.printf("接收到的结果是%s", res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });
    }
}
