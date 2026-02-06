package com.crawdwall_backend_api.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CompanyCreateRequest(
    @JsonProperty(required = true) String companyName,
    @JsonProperty(required = true) String companyEmail,
    @JsonProperty(required = true) String password,
    @JsonProperty(required = true) String confirmPassword
) {
    
}
