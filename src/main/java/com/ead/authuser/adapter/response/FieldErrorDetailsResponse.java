package com.ead.authuser.adapter.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record FieldErrorDetailsResponse(@JsonProperty("name")
                                        String name,
                                        @JsonProperty("message")
                                        String message) {

    @Builder(toBuilder = true)
    public FieldErrorDetailsResponse{}

}
