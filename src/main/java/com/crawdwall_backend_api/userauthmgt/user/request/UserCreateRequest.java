package com.crawdwall_backend_api.userauthmgt.user.request;

import com.crawdwall_backend_api.userauthmgt.user.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

@Builder
public record UserCreateRequest(

        @JsonProperty(required = true) String firstName,
        @JsonProperty(required = true) String lastName,
        @JsonProperty(required = true) String emailAddress,
        @JsonProperty(required = true) String password,
        @JsonProperty(required = true) UserType userType,
        @JsonProperty(required = true) String profileColourCode,
        @JsonProperty(required = true) String phoneNumber

                ) {
}
