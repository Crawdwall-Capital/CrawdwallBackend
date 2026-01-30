package com.crawdwall_backend_api.userauthmgt.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserAuthRequest(
        @JsonProperty(required = true) String emailAddress,
        @JsonProperty(required = true) String password
) {
}
