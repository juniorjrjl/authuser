package com.ead.authuser.adapter.outbound.persistence;

import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity;
import com.ead.authuser.adapter.outbound.persistence.entity.UserEntity_;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID>, JpaSpecificationExecutor<UserEntity> {

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    @EntityGraph(attributePaths = UserEntity_.ROLES, type = FETCH)
    Optional<UserEntity> findByUsername(final String username);

    @EntityGraph(attributePaths = UserEntity_.ROLES, type = FETCH)
    Optional<UserEntity> findById(final UUID id);

}
