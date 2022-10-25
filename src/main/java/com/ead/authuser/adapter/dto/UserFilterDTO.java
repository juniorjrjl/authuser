package com.ead.authuser.adapter.dto;

import com.ead.authuser.core.domain.enumeration.UserStatus;
import com.ead.authuser.core.domain.enumeration.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public record UserFilterDTO(@JsonProperty("userType") UserType userType,
                            @JsonProperty("userStatus") UserStatus userStatus,
                            @JsonProperty("email") String email,
                            @JsonProperty("fullName") String fullName) {

    @Builder(toBuilder = true)
    public UserFilterDTO {

    }

    public String likeEmail(){
        return String.format("%%%s%%", email);
    }
    
    public String likeFullName(){
        return String.format("%%%s%%", fullName);
    }
    
}
