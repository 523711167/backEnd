package org.pdaodao.demo5;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitComsumer2 {

    public static final String EXCHANGE_NAME = "log";

    /**
     *  1。 声明channel，使用queueBind绑定队列和交换机
     *  2。 basicConsume消费队列消息，关闭自动应答
     *  3。 使用basicAck完成自动应答
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        //channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");

        channel.basicConsume(queue, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.println(res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });
    }
}
