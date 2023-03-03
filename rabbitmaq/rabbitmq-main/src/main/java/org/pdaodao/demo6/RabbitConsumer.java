package org.pdaodao.demo6;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {

    public static final String EXCHANGE_NAME = "log_direct";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "red");

        channel.basicConsume(queue, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.println(res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });

    }
}
