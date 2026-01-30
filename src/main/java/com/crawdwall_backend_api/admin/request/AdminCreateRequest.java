package com.crawdwall_backend_api.admin.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AdminCreateRequest(
    @JsonProperty(required = true) String firstName,
    @JsonProperty(required = true) String lastName,
    @JsonProperty(required = true) String emailAddress,
    @JsonProperty(required = true) String roleId
) {
    
}

