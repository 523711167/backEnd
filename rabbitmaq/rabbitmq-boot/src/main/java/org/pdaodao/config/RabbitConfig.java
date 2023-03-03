package org.pdaodao.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_X = "exchangex";
    public static final String EXCHANGE_Y = "exchangey";

    public static final String QUEUE_A = "queuea";
    public static final String QUEUE_B = "queueb";
    public static final String QUEUE_D = "queued";

    public static final String ROUTE_A = "routea";
    public static final String ROUTE_B = "routeb";
    public static final String ROUTE_D = "routed";

    @Bean(value = "xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(EXCHANGE_X, true, false, null);
    }

    @Bean(value = "yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(EXCHANGE_Y, true, false, null);
    }

    @Bean(value = "aQueue")
    public Queue aQueue() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", EXCHANGE_Y);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", ROUTE_D);
        //声明队列的 TTL
        args.put("x-message-ttl", 10000);
        return QueueBuilder.durable(QUEUE_A).withArguments(args).build();
    }

    @Bean(value = "bQueue")
    public Queue bQueue() {
        Map<String, Object> args = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        args.put("x-dead-letter-exchange", EXCHANGE_Y);
        //声明当前队列的死信路由 key
        args.put("x-dead-letter-routing-key", ROUTE_D);
        //声明队列的 TTL
        args.put("x-message-ttl", 1000);
        return QueueBuilder.durable(QUEUE_B).withArguments(args).build();
    }

    @Bean(value = "dQueue")
    public Queue dQueue() {
        return QueueBuilder.durable(QUEUE_D).build();
    }

    @Bean(value = "aRoute")
    public Binding aRoute(@Qualifier("xExchange") Exchange exchange, @Qualifier("aQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTE_A).noargs();
    }

    @Bean(value = "bRoute")
    public Binding bRoute(@Qualifier("xExchange") Exchange exchange, @Qualifier("bQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTE_B).noargs();
    }

    @Bean(value = "dRoute")
    public Binding dRoute(@Qualifier("yExchange") Exchange exchange, @Qualifier("dQueue") Queue queue){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTE_D).noargs();
    }
}
