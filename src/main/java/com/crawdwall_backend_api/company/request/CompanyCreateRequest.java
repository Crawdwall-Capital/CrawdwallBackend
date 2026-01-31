package com.crawdwall_backend_api.company.request;

import com.crawdwall_backend_api.company.CompanyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CompanyCreateRequest(
    @JsonProperty(required = true) String companyName,
    @JsonProperty(required = true) String companyEmail,
    @JsonProperty(required = true) String companyPhone,
    @JsonProperty(required = true) String companyWebsite,
    @JsonProperty(required = true) String companyLogo,
    @JsonProperty(required = true) String companyRegistrationNumber,
    @JsonProperty(required = true) String companyRegistrationDate,
    @JsonProperty(required = true) CompanyType companyType,
    @JsonProperty(required = true) String password
) {
    
}
