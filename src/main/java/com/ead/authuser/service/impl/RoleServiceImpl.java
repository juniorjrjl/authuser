package com.ead.authuser.service.impl;

import com.ead.authuser.enumeration.RoleType;
import com.ead.authuser.model.RoleModel;
import com.ead.authuser.repository.RoleRepository;
import com.ead.authuser.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Optional<RoleModel> findByRoleName(final RoleType roleType) {
        return roleRepository.findByRoleName(roleType);
    }
}
