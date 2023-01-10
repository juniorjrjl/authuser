package com.ead.authuser.adapter.service.decorator;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.port.UserQueryServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserQueryServicePortImplDecorator implements UserQueryServicePort {

    private final UserQueryServicePort userQueryServicePort;

    @Override
    public List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo) {
        return userQueryServicePort.findAll(domain, pageInfo);
    }

    @Transactional
    @Override
    public UserDomain findById(final UUID id) {
        return userQueryServicePort.findById(id);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userQueryServicePort.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userQueryServicePort.existsByEmail(email);
    }

}
