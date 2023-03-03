package org.pdaodao.config;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.pdaodao.config.RabbitConfig.QUEUE_D;


@Component
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = QUEUE_D)
    public void receiveD(Message message, Channel channel) {
        String msg = new String(message.getBody());
        System.out.println("当前时间：" + new Date() + ",收到死信队列信息{}" + msg);
    }
}
