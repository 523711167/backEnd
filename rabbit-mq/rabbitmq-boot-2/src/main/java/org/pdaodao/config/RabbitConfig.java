package org.pdaodao.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_NORMAL = "normal";
    public static final String EXCHANGE_BACKUP = "backup";

    public static final String QUEUE_CONFIRM = "confirmQueue";
    public static final String QUEUE_BACKUP = "backupQueue";
    public static final String QUEUE_WARN = "warnQueue";

    public static final String ROUTE = "key1";

    @Bean("normal")
    public DirectExchange normal() {
        Map<String, Object> args = new HashMap<>();
        args.put("alternate-exchange", EXCHANGE_BACKUP);
        return new DirectExchange(EXCHANGE_NORMAL, true, false, args);
    }

    @Bean("backup")
    public FanoutExchange backup() {
        return new FanoutExchange(EXCHANGE_BACKUP);
    }

    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(QUEUE_CONFIRM).build();
    }

    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(QUEUE_BACKUP).build();
    }

    @Bean("warnQueue")
    public Queue warnQueue() {
        return QueueBuilder.durable(QUEUE_WARN).build();
    }

    @Bean
    public Binding a() {
        return BindingBuilder.bind(confirmQueue()).to(normal()).with(ROUTE);
    }

    @Bean
    public Binding b() {
        return BindingBuilder.bind(backupQueue()).to(backup());
    }

    @Bean
    public Binding c() {
        return BindingBuilder.bind(warnQueue()).to(backup());
    }
}
