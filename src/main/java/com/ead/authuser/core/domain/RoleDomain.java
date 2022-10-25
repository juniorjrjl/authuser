package com.ead.authuser.core.domain;

import com.ead.authuser.core.domain.enumeration.RoleType;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;

import java.util.UUID;

public record RoleDomain (
        UUID id,
        RoleType roleName
)implements GrantedAuthority {

    @Builder(toBuilder = true)
    public RoleDomain {
    }

    @Override
    public String getAuthority() {
        return this.roleName.toString();
    }

}
