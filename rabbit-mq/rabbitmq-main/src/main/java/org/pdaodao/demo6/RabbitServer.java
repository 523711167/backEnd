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
     *
     *  mandatory：一个布尔值，指示当消息无法被正确路由到目标队列时的处理方式。如果设置为 true，
     *  则消息无法路由到目标队列时将触发returnListeners，可以通过注册returnListeners来处理这些无法路由的消息。
     */
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = U.createCon().createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //发布确认机制 (同步机制)
        //可以保证消息是否发送到交换机上(或者说是broke)
        //channel.confirmSelect();和channel.waitForConfirms();
        channel.confirmSelect();

        //在basicPublish设置mandatory和immediate属性为true,如果routeKey找不到队列，就执行回调函数
        //ReturnCallback和ReturnListener，文档建议使用ReturnCallback可以用lambda表达式
        channel.addReturnListener(returnMessage -> {
            System.out.println(returnMessage);
        });

        //发布确认机制 (异步机制)
        //可以保证消息是否发送到交换机上(或者说是broke)
        //可以和同步确认机制同时使用
//        channel.addConfirmListener(( deliveryTag,  multiple) -> {
//            System.out.println("success");
//        }, ( deliveryTag, multiple) -> {
//            System.out.println("fail");
//        });

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            try {
                String msg = scanner.next();
                String[] split = msg.split(",");
                String routeKey = split[1];
                channel.basicPublish(EXCHANGE_NAME, routeKey, false, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes());
                boolean b = channel.waitForConfirms(1000);
                System.out.println("b = " + b);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
