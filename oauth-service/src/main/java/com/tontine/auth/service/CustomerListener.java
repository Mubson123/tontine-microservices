package com.tontine.auth.service;

import com.google.protobuf.InvalidProtocolBufferException;
import com.tontine.customer.proto.CustomerEvent;
import com.tontine.auth.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerListener {
    private final UserService userService;

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void onMessage(byte[] payload) throws InvalidProtocolBufferException {
        CustomerEvent event = CustomerEvent.parseFrom(payload);
        userService.createUserFromCustomer(event);
    }
}
