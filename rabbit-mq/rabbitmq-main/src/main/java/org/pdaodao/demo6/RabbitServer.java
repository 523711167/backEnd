package org.pdaodao.demo6;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.pdaodao.U;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RabbitServer {

    public static final String EXCHANGE_NAME = "log_direct";

    /**
     *  direct交换机
     *  1。通过routekey控制，交换机和queue的绑定Œ
     */
    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String msg = scanner.next();
            String[] split = msg.split(",");
            String routeKey = split[1];
            channel.basicPublish(EXCHANGE_NAME, routeKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
        }

    }
}
