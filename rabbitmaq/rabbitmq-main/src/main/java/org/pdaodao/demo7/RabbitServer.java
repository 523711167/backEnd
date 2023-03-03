package org.pdaodao.demo7;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.pdaodao.U;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitServer {

    public static final String EXCHANGE_TOPIC = "log_topic";

    /**
     * topic交换机
     *  1。 *代表一个单词。
     *  2。 #代表0或者多个单词
     *
     * topic有点像fanout和derect的结合
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        channel.exchangeDeclare(EXCHANGE_TOPIC, BuiltinExchangeType.TOPIC);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            String[] split = msg.split(",");
            String routeKey = split[1];
            channel.basicPublish(EXCHANGE_TOPIC, routeKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
        }

    }
}
