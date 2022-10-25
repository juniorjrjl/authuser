package com.ead.authuser.adapter.service.decorator;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.port.UserServicePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserServicePortImplDecorator implements UserServicePort {

    private final UserServicePort userServicePort;

    @Override
    public List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo) {
        return userServicePort.findAll(domain, pageInfo);
    }

    @Override
    public Optional<UserDomain> findById(final UUID id) {
        return userServicePort.findById(id);
    }

    @Override
    public void delete(final UserDomain userDomain) {
        userServicePort.delete(userDomain);
    }

    @Transactional
    @Override
    public void deleteAndPublish(final UserDomain userDomain) {
        userServicePort.deleteAndPublish(userDomain);
    }

    @Override
    public UserDomain save(final UserDomain userDomain) {
        return userServicePort.save(userDomain);
    }

    @Transactional
    @Override
    public UserDomain saveAndPublish(final UserDomain userDomain) {
        return userServicePort.saveAndPublish(userDomain);
    }

    @Transactional
    @Override
    public UserDomain updateAndPublish(final UserDomain userDomain) {
        return userServicePort.updateAndPublish(userDomain);
    }

    @Override
    public UserDomain updatePassword(final UserDomain userDomain) {
        return userServicePort.updatePassword(userDomain);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userServicePort.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userServicePort.existsByEmail(email);
    }
}
