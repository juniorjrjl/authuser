package com.ead.authuser.core.domain;

public record UserInsertDomain(String username, String email, String password, String fullName, String phoneNumber, String cpf) {
}
