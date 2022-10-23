package com.ead.authuser.repository;

import com.ead.authuser.model.UserModel;
import com.ead.authuser.model.UserModel_;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

public interface UserRepository extends JpaRepository<UserModel, UUID>, JpaSpecificationExecutor<UserModel> {

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    @EntityGraph(attributePaths = UserModel_.ROLES, type = FETCH)
    Optional<UserModel> findByUsername(final String username);

    @EntityGraph(attributePaths = UserModel_.ROLES, type = FETCH)
    Optional<UserModel> findById(final UUID id);

}
