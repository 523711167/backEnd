package org.pdaodao.demo8;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.pdaodao.U;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitConsumer {

    public static final String EXCHANGE_DIRECT = "log_direct";

    public static final String EXCHANGE_DEAD = "log_dead";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = U.createCon().createChannel();

//        channel.exchangeDeclare(EXCHANGE_DEAD, BuiltinExchangeType.DIRECT);

        //正常队列绑定死信队列信息
        Map<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", EXCHANGE_DEAD);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "lisi");

        String queue1 = "queue1";
        channel.queueDeclare(queue1, true, false, false, params);
        channel.queueBind(queue1, EXCHANGE_DIRECT, "woshinibaba");

        channel.basicConsume(queue1, true, (s, delivery) -> {
            String res = new String(delivery.getBody());
            System.out.printf("消费者消费到了，%s", res);
        }, (s, e) -> {
            System.out.println("消息中断");
        });

    }
}
