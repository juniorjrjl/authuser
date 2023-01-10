package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.PageInfo;
import com.ead.authuser.core.domain.UserDomain;
import com.ead.authuser.core.domain.UserFilterDomain;

import java.util.List;
import java.util.UUID;

public interface UserQueryServicePort {

    List<UserDomain> findAll(final UserFilterDomain domain, final PageInfo pageInfo);

    UserDomain findById(final UUID id);

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

}
