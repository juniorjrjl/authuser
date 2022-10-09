package com.ead.authuser.publisher;

import com.ead.authuser.dto.UserEventDTO;
import com.ead.authuser.enumeration.ActionType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeUserEvent;

    public UserEventPublisher(final RabbitTemplate rabbitTemplate,
                          @Value("${ead.broker.exchange.userEvent}")
                          final String exchangeUserEvent) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeUserEvent = exchangeUserEvent;
    }

    public void publishUserEvent(final UserEventDTO userEvent, final ActionType actionType){
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userEvent.toBuilder().actionType(actionType.toString()).build());
    }
}