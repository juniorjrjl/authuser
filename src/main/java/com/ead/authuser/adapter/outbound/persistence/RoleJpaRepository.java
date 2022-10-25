package com.ead.authuser.adapter.outbound.persistence;

import com.ead.authuser.adapter.outbound.persistence.entity.RoleEntity;
import com.ead.authuser.core.domain.enumeration.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByRoleName(final RoleType name);

}
