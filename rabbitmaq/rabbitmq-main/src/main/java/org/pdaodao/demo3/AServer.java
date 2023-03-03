package org.pdaodao.demo3;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.pdaodao.U;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class AServer {

    private final static String QUEUE_NAME = "hello";

    /**
     * queue两种状态
     *  ready   等待消费状态
     *  unacked 等待应答状态
     * channel.basicQos(2); 表示通道上允许的未确认消息的最大数量
     * 通过设置Qos，实现不公平分发消息
     *
     * 业务场景
     *  A消费需要10秒 b消费需要1秒
     *  hello队列发送10条消息，各5条进入A，B信道，如果A信道断开，会被B信道消费
     *
     *  MessageProperties.PERSISTENT_TEXT_PLAIN设置消息的持久化，前提队列必须要持久化
     *
     *  channel.basicConsume(QUEUE_NAME, false,
     *  autoAck 关闭自动应答
     *  channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        //durable 队列持久化
        //exclusive 独有的
        //autoDelete 队列消失自动删除
        channel.queueDeclare(QUEUE_NAME, false, false, true, null);

        //从控制台当中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("发送消息完成:" + message);
        }
    }
}
