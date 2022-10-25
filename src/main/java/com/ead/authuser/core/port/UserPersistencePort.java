package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserPersistencePort {

    List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo);
    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    Optional<UserDomain> findByUsername(final String username);

    Optional<UserDomain> findById(final UUID id);

    void delete(final UserDomain userDomain);

    UserDomain save(final UserDomain userDomain);

}
