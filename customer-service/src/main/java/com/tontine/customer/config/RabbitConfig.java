package com.tontine.customer.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "customer.exchange";
    public static final String ROUTING_KEY = "customer.created";

    @Bean
    TopicExchange customerExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
}
