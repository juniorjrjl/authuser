package com.ead.authuser.service;

import com.ead.authuser.enumeration.RoleType;
import com.ead.authuser.model.RoleModel;

import java.util.Optional;

public interface RoleService {

    Optional<RoleModel> findByRoleName(final RoleType roleType);

}
