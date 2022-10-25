package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserServicePort {

    List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo);

    Optional<UserDomain> findById(final UUID id);

    void delete(final UserDomain userDomain);

    void deleteAndPublish(final UserDomain userDomain);

    UserDomain save(final UserDomain userDomain);

    UserDomain saveAndPublish(UserDomain userDomain);

    UserDomain updateAndPublish(final UserDomain userDomain);

    UserDomain updatePassword(final UserDomain userDomain);

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

}
