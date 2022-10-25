package com.ead.authuser.adapter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtDTO {

    private final String token;
    private final String type = "Bearer";

    public JwtDTO(@JsonProperty("token") final String token) {
        this.token = token;
    }
}
