package com.ead.authuser.core.port;

import com.ead.authuser.core.domain.RoleDomain;
import com.ead.authuser.core.domain.enumeration.RoleType;

public interface RoleQueryServicePort {

    RoleDomain findByRoleName(final RoleType roleType);

}
