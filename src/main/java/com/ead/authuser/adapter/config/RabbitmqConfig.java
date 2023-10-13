package com.ead.authuser.adapter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    private final CachingConnectionFactory cachingConnectionFactory;
    private final String exchangeUserEvent;

    public RabbitmqConfig(final CachingConnectionFactory cachingConnectionFactory,
                          @Value("${ead.broker.exchange.user-event}")
                          final String exchangeUserEvent) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.exchangeUserEvent = exchangeUserEvent;
    }


    @Bean
    public RabbitTemplate rabbitTemplate(){
        var template = new RabbitTemplate(cachingConnectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public FanoutExchange fanoutUserEvent(){
        return new FanoutExchange(exchangeUserEvent);
    }

}
