package org.pdaodao.demo8;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer1 {

    public static final String EXCHANGE_DIRECT = "log_direct";

    public static final String EXCHANGE_DEAD = "log_dead";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        channel.exchangeDeclare(EXCHANGE_DEAD, BuiltinExchangeType.DIRECT);

        String queue = "dead_queue";
        channel.queueDeclare(queue, false, false, false, null);
        channel.queueBind(queue, EXCHANGE_DEAD, "lisi");

        channel.basicConsume(queue, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.printf("死信队列消费到了，%s", res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });

    }
}
