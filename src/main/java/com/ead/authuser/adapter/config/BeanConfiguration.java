package com.ead.authuser.adapter.config;

import com.ead.authuser.AuthuserApplication;
import com.ead.authuser.adapter.outbound.persistence.RolePersistencePortImpl;
import com.ead.authuser.adapter.service.decorator.UserQueryServicePortImplDecorator;
import com.ead.authuser.adapter.service.decorator.UserServicePortImplDecorator;
import com.ead.authuser.core.port.RoleQueryServicePort;
import com.ead.authuser.core.port.UserEventPublisherPort;
import com.ead.authuser.core.port.UserPersistencePort;
import com.ead.authuser.core.port.UserQueryServicePort;
import com.ead.authuser.core.port.UserServicePort;
import com.ead.authuser.core.service.RoleQueryServicePortImpl;
import com.ead.authuser.core.service.UserQueryServicePortImpl;
import com.ead.authuser.core.service.UserServicePortImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@ComponentScan(basePackageClasses = AuthuserApplication.class)
public class BeanConfiguration {

    @Primary
    @Bean
    public UserQueryServicePort userQueryServicePortImpl(final UserPersistencePort persistence){
        var userQueryServicePort = new UserQueryServicePortImpl(persistence);
        return new UserQueryServicePortImplDecorator(userQueryServicePort);
    }

    @Primary
    @Bean
    public UserServicePort userServicePortImpl(final UserPersistencePort persistence, final UserQueryServicePort userQueryServicePort,
                                               final UserEventPublisherPort publisher, final RoleQueryServicePort roleQueryServicePort){
        var userServicePortImpl = new UserServicePortImpl(persistence, userQueryServicePort, publisher, roleQueryServicePort);
        return new UserServicePortImplDecorator(userServicePortImpl);
    }

    @Primary
    @Bean
    public RoleQueryServicePort roleServicePortImpl(final RolePersistencePortImpl persistence){
        return new RoleQueryServicePortImpl(persistence);
    }

}
