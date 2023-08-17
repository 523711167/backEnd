package org.pdaodao.demo6;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {

    public static final String EXCHANGE_NAME = "log_direct";
    public static final String QUEUE_NAME = "log_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        //队列对应多个消费者只启用一个人
//        Map<String, Object> params = new HashMap<>();
//        params.put("x-single-active-consumer", true);
//        channel.queueDeclare(QUEUE_NAME,false,false,false,params);

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "red");

        channel.basicConsume(QUEUE_NAME, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.println(res);
        }, s -> {
            System.out.println("CancelCallback");
        });

    }
}
