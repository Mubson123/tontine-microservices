package com.tontine.oauth.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {
    public static final String CUSTOMER_EXCHANGE = "customer.exchange";
    public static final String QUEUE = "auth.customer.created.queue";
    public static final String ROUTING_KEY = "customer.created";

    @Bean
    Queue customerCreatedQueue() {
        return new Queue(
                QUEUE,
                true,
                false,
                false
        );
    }

    @Bean
    TopicExchange customerExchange() {
        return new TopicExchange(CUSTOMER_EXCHANGE, true, false);
    }

    @Bean
    Binding customerCreatedBinding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }
}
