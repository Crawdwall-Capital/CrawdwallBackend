package com.crawdwall_backend_api.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.crawdwall_backend_api.company.CompanyType;

import lombok.Builder;

@Builder
public record CompanyUpdateRequest(
    String companyName,
    String companyEmail,
    String companyPhone,
    String companyWebsite,
    String companyLogo,
    String companyRegistrationNumber,
    String companyRegistrationDate,
    CompanyType companyType
) {
    
}
