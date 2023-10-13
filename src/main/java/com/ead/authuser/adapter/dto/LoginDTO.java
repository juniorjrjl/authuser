package com.ead.authuser.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;


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
