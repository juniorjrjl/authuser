package com.ead.authuser.adapter.config;

import com.ead.authuser.AuthuserApplication;
import com.ead.authuser.adapter.outbound.persistence.RolePersistencePortImpl;
import com.ead.authuser.adapter.service.decorator.UserServicePortImplDecorator;
import com.ead.authuser.core.port.RoleServicePort;
import com.ead.authuser.core.port.UserEventPublisherPort;
import com.ead.authuser.core.port.UserPersistencePort;
import com.ead.authuser.core.port.UserServicePort;
import com.ead.authuser.core.service.RoleServicePortImpl;
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
    public UserServicePort userServicePortImpl(final UserPersistencePort persistence, final UserEventPublisherPort publisher){
        var userServicePortImpl = new UserServicePortImpl(persistence, publisher);
        return new UserServicePortImplDecorator(userServicePortImpl);
    }

    @Bean
    public RoleServicePort roleServicePortImpl(final RolePersistencePortImpl persistence){
        return new RoleServicePortImpl(persistence);
    }

}
