package com.ead.authuser.adapter.outbound.publish;

import com.ead.authuser.adapter.mapper.UserMapper;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.enumeration.ActionType;
import com.ead.authuser.core.port.UserEventPublisherPort;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserEventPublishPortImpl implements UserEventPublisherPort {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeUserEvent;

    private final UserMapper userMapper;

    public UserEventPublishPortImpl(final RabbitTemplate rabbitTemplate,
                                    @Value("${ead.broker.exchange.userEvent}")
                                    final String exchangeUserEvent,
                                    final UserMapper userMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeUserEvent = exchangeUserEvent;
        this.userMapper = userMapper;
    }

    @Override
    public void publish(final UserDomain domain, final ActionType actionType) {
        rabbitTemplate.convertAndSend(exchangeUserEvent, "", userMapper.toEventDTO(domain, actionType));
    }
}
