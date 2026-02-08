package com.crawdwall_backend_api.company.request;


import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.company.CompanySocialMediaType;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record CompanyUpdateRequest(
    String companyName,
    String companyPhone,
    String companyWebsite,
    LocalDate companyEstablishedDate,
    CompanyType companyType,
    CompanySocialMediaType companySocialMediaType,
    String companySocialMediaUrl
) {
    
}
