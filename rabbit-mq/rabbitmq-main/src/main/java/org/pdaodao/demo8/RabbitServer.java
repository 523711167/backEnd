package org.pdaodao.demo8;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitServer {

    public static final String EXCHANGE_DIRECT = "log_direct";

    /**
     *  死信队列
     *  1。排队超时 new AMQP.BasicProperties().builder().expiration("10000").build(); 设置10s的超时时间，进入死信队列
     *  2。队列长度,params.put("x-max-length", 6); 设置队列ready长度为6，超过进入死信队列
     *  3。消费者取消自动应答,并且拒绝重新入队，直接进入死信队列 channel.basicReject(delivery.getEnvelope().getDeliveryTag(), false);
     *
     *  服务端
     *  1。创建channel，basicPublish发布消息
     *
     *  消费者
     *  1。创建channel，创建队列queueDeclare，
     *  2。设置死信队列和正常队列的对应关系
     *  3。绑定路由和交换机的关系
     *  4。basicConsume消费队列消息
     *
     *  延迟队列
     *      是死信队列的另一种，不设置消费者，经过ttl时间，路由到死信队列中
     *
     *  优先队列
     *   1。声明queueDeclare队列的时候，设置x-max-priority区间
     *   2。每次发送消息的时候，设置消息的优先级AMQP.BasicProperties().builder().priority(5).build();
     *
     *  惰性队列
     *  1. 声明queueDeclare队列的时候，设置x-queue-mode模式
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        channel.exchangeDeclare(EXCHANGE_DIRECT, BuiltinExchangeType.DIRECT);

        AMQP.BasicProperties build = new AMQP.BasicProperties().builder().expiration("10000").build();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            channel.basicPublish(EXCHANGE_DIRECT, "woshinibaba", build, msg.getBytes());
        }
    }
}
