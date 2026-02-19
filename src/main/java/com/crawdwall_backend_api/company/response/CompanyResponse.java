package com.crawdwall_backend_api.company.response;


import com.crawdwall_backend_api.company.CompanyType;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.crawdwall_backend_api.utils.Address;
import com.crawdwall_backend_api.utils.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.crawdwall_backend_api.company.CompanySocialMediaType;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

    String id;
    String companyName;
    String companyEmail;
    String companyPhone;
    String companyWebsite;
    String companyLogo;
    String companyRegistrationNumber;
    LocalDate companyEstablishedDate;
    CompanyType companyType;
    CompanySocialMediaType companySocialMediaType;
    String companySocialMediaUrl;
    String userId;
    Address companyAddress;
    boolean isActive;
    boolean isDeleted;
    boolean isVerified;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Status status;
    String countryOfRegistration;
    String otherInformationForCompanyType;
}
