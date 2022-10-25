package com.ead.authuser.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

public record LoginDTO(@NotBlank
                       @JsonProperty("username")
                       String username,
                       @NotBlank
                       @JsonProperty("password")
                       String password) {

    @Builder
    public LoginDTO {
    }
}
