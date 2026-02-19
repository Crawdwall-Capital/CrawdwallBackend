package com.crawdwall_backend_api.company.request;

import com.crawdwall_backend_api.company.CompanySocialMediaType;
import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.utils.Address;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record CompanyProfileSetUpCreateRequest(

     String companyName,
    @JsonProperty(required = true)  CompanyType companyType,
    String otherInformationForCompanyType,
    @JsonProperty(required = true) String country,
     @JsonProperty(required = true)
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
     LocalDate establishedDate,
     String companyWebsite,
    @JsonProperty(required = true) String companyPhone,
    @JsonProperty(required = true) String companyAddress,
    @JsonProperty(required = true) CompanySocialMediaType companySocialMediaType,
    @JsonProperty(required = true) String companySocialMediaUrl,
    @JsonProperty(required = true)    Address address

) {
    
}
