package com.tontine.customer.service.impl;

import com.tontine.customer.config.RabbitConfig;
import com.tontine.customer.models.Customer;
import com.tontine.customer.proto.CustomerEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class CustomerEventPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void publishCustomerEvent(Customer customer, String eventType) {
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        CustomerEvent event = customerEvent(customer, eventType);
                        rabbitTemplate.convertAndSend(
                                RabbitConfig.EXCHANGE,
                                RabbitConfig.ROUTING_KEY,
                                event.toByteArray()
                        );
                    }
                }
        );
    }

    private CustomerEvent customerEvent(Customer customer, String eventType) {
        return CustomerEvent.newBuilder()
                .setCustomerId(customer.getId().toString())
                .setFirstname(customer.getFirstname())
                .setLastname(customer.getLastname())
                .setEmail(customer.getEmail())
                .setEventType(eventType)
                .setCreatedAt(Instant.now().toEpochMilli())
                .build();
    }
}
