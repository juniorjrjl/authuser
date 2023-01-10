package com.ead.authuser.core.service;

import com.ead.authuser.core.domain.RoleDomain;
import com.ead.authuser.core.domain.enumeration.RoleType;
import com.ead.authuser.core.exception.DomainNotFoundException;
import com.ead.authuser.core.port.RolePersistencePort;
import com.ead.authuser.core.port.RoleQueryServicePort;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RoleQueryServicePortImpl implements RoleQueryServicePort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public RoleDomain findByRoleName(final RoleType roleType) {
        return rolePersistencePort.findByRoleName(roleType)
                .orElseThrow(() -> new DomainNotFoundException(String.format("A role %s não foi encontrada", roleType)));
    }

}
