package com.ead.authuser.core.domain;

import lombok.Builder;

import java.time.OffsetDateTime;

public record UserUpdateDomain(String fullName, String phoneNumber, String cpf) {

    @Builder(toBuilder = true)
    public UserUpdateDomain {}
}
