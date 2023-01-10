package com.ead.authuser.core.service;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;
import com.ead.authuser.core.exception.DomainNotFoundException;
import com.ead.authuser.core.port.UserPersistencePort;
import com.ead.authuser.core.port.UserQueryServicePort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class UserQueryServicePortImpl implements UserQueryServicePort {

    private final UserPersistencePort userPersistencePort;

    @Override
    public List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo) {
        return userPersistencePort.findAll(domain, pageInfo);
    }

    @Override
    public UserDomain findById(final UUID id) {
        return userPersistencePort.findById(id)
                .orElseThrow(() -> new DomainNotFoundException(String.format("Usuário com id %s não encontrado", id)));
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
