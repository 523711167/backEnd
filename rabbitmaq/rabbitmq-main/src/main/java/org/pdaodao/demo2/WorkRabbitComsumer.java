package org.pdaodao.demo2;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkRabbitComsumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();
        channel.basicConsume(QUEUE_NAME, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.println(res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });
    }
}
