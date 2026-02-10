package com.crawdwall_backend_api.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CompanySignatoryRequest(
    @JsonProperty(required = true) String signatoryName,
    @JsonProperty(required = true) String role,
    @JsonProperty(required = true) String email,
    @JsonProperty(required = true) String phoneNumber
) {
    
}
