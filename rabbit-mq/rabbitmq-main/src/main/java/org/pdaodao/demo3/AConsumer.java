package org.pdaodao.demo3;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AConsumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();


        channel.basicQos(5);
        channel.basicConsume(QUEUE_NAME, false, (s, delivery) -> {
            String res = new String(delivery.getBody());
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("接受到消息为" + res);

            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }, (s, e) -> {
            System.out.println("拒接接受消息");
        });
    }
}
