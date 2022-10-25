package com.ead.authuser.adapter.outbound.persistence;

import com.ead.authuser.adapter.mapper.RoleMapper;
import com.ead.authuser.core.domain.RoleDomain;
import com.ead.authuser.core.domain.enumeration.RoleType;
import com.ead.authuser.core.port.RolePersistencePort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class RolePersistencePortImpl implements RolePersistencePort {

    private final RoleJpaRepository roleJpaRepository;
    private final RoleMapper roleMapper;

    @Override
    public Optional<RoleDomain> findByRoleName(final RoleType name) {
        var entity = roleJpaRepository.findByRoleName(name);
        return entity.map(roleMapper::toDomain);
    }

}
