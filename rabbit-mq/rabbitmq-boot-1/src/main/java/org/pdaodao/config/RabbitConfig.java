package org.pdaodao.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    @Bean("cExchange")
    public DirectExchange cExchange() {
        return new DirectExchange(CONFIRM_EXCHANGE_NAME);
    }

    @Bean("cQueue")
    public Queue cQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    @Bean("cBind")
    public Binding cBind(@Qualifier("cExchange") DirectExchange exchange, @Qualifier("cQueue") Queue queue) {
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }
}
