package com.crawdwall_backend_api.company.companyKycVerification.request;

import com.crawdwall_backend_api.company.CompanyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CompanyProfileKycRequest(
    @JsonProperty(required = true) String companyName,
    @JsonProperty(required = true) CompanyType companyType,
    @JsonProperty(required = true) String countryOfRegistration,
    @JsonProperty(required = true) LocalDate dateEstablished,
    @JsonProperty(required = true) String companyAddress,
    String companyWebsite,
    String socialMediaLinks, // JSON string or comma-separated links
    @JsonProperty(required = true) String companyEmail,
    @JsonProperty(required = true) String companyPhone
) {
    
}