package org.pdaodao.demo2;

import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class WorkRabbitServer {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //从控制台当中接受信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println("发送消息完成:" + message);
        }
    }
}
