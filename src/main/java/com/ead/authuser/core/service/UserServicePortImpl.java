package com.ead.authuser.core.service;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.port.UserEventPublisherPort;
import com.ead.authuser.core.port.UserPersistencePort;
import com.ead.authuser.core.port.UserServicePort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.ead.authuser.core.domain.enumeration.ActionType.CREATE;
import static com.ead.authuser.core.domain.enumeration.ActionType.DELETE;
import static com.ead.authuser.core.domain.enumeration.ActionType.UPDATE;

@AllArgsConstructor
public class UserServicePortImpl implements UserServicePort {

    private final UserPersistencePort userPersistencePort;
    private final UserEventPublisherPort userEventPublisherPort;

    @Override
    public List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo) {
        return userPersistencePort.findAll(domain, pageInfo);
    }

    @Override
    public Optional<UserDomain> findById(final UUID id) {
        return userPersistencePort.findById(id);
    }

    @Override
    public void delete(final UserDomain userDomain) {
        userPersistencePort.delete(userDomain);
    }

    @Override
    public void deleteAndPublish(final UserDomain userDomain) {
        delete(userDomain);
        userEventPublisherPort.publish(userDomain, DELETE);
    }

    @Override
    public UserDomain save(final UserDomain userDomain) {
        return userPersistencePort.save(userDomain);
    }

    @Override
    public UserDomain saveAndPublish(final UserDomain userDomain) {
        var saved = save(userDomain);
        userEventPublisherPort.publish(userDomain, CREATE);
        return saved;
    }

    @Override
    public UserDomain updateAndPublish(final UserDomain userDomain) {
        var saved = save(userDomain);
        userEventPublisherPort.publish(saved, UPDATE);
        return saved;
    }

    @Override
    public UserDomain updatePassword(final UserDomain userDomain) {
        return save(userDomain);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userPersistencePort.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(final String email) {
        return userPersistencePort.existsByEmail(email);
    }

}
