package com.ead.authuser.core.domain;

import com.ead.authuser.core.domain.enumeration.UserStatus;
import com.ead.authuser.core.domain.enumeration.UserType;
import lombok.Builder;

public record UserFilterDomain(UserType userType,
                               UserStatus userStatus,
                               String email,
                               String fullName) {

    @Builder(toBuilder = true)
    public UserFilterDomain{

    }

}
