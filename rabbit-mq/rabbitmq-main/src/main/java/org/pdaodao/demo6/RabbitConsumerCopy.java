package org.pdaodao.demo6;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * direct交换机red路由对多个队列，出现队列重复消费
 * 我现在需要轮流消费
 * 创建多个消费者对应一个队列
 */
public class RabbitConsumerCopy {

    public static final String EXCHANGE_NAME = "log_direct";
    public static final String QUEUE_NAME = "log_queue";
    //应为basicNack之后，被别的消费者消费，下一个消息又轮到他了
    //所以让人误以为某个队列会优先消费
    private static int i = 0;

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        //exclusive = true 表示channel和queue是一对一的 如果创建新channel会抛出异常
//        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "red");

        channel.basicConsume(QUEUE_NAME, false, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.println(res);

            //不主动应答消息就是Unacked状态,会被重新消费

            //主动应答
            //requeue = false 直接丢弃
//            System.out.println("channel.basicNack = " + res);
//            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);

            //主动应答
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

            i++;
        }, s -> {
            System.out.println("CancelCallback");
        });

    }
}
