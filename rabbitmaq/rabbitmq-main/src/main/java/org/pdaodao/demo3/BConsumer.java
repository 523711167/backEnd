package org.pdaodao.demo3;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BConsumer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        channel.basicQos(2);
        channel.basicConsume(QUEUE_NAME, false, (s, delivery) -> {
            String res = new String(delivery.getBody());
            try {
                TimeUnit.SECONDS.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "接受到消息为" + res);


            //channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }, (s, e) -> {
            System.out.println("拒接接受消息");
        });
    }
}
