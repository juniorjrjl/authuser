package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.RoleDomain;
import com.ead.authuser.core.domain.enumeration.RoleType;

import java.util.Optional;

public interface RoleServicePort {

    Optional<RoleDomain> findByRoleName(final RoleType roleType);

}
