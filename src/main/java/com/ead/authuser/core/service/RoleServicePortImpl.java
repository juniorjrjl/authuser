package com.ead.authuser.core.service;

import com.ead.authuser.core.domain.RoleDomain;
import com.ead.authuser.core.domain.enumeration.RoleType;
import com.ead.authuser.core.port.RolePersistencePort;
import com.ead.authuser.core.port.RoleServicePort;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class RoleServicePortImpl implements RoleServicePort {

    private final RolePersistencePort rolePersistencePort;

    @Override
    public Optional<RoleDomain> findByRoleName(final RoleType roleType) {
        return rolePersistencePort.findByRoleName(roleType);
    }

}
