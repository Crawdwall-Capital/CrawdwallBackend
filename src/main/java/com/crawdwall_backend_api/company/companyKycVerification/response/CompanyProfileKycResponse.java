package com.crawdwall_backend_api.company.companyKycVerification.response;

import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.company.companyKycVerification.KycStepStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfileKycResponse {
    
    private String id;
    private String companyId;
    
    // Organization Details
    private String companyName;
    private CompanyType companyType;
    private String countryOfRegistration;
    private LocalDate dateEstablished;
    
    // Contact Information
    private String companyAddress;
    private String companyWebsite;
    private String socialMediaLinks;
    private String companyEmail;
    private String companyPhone;
    
    // Step Status
    private KycStepStatus stepStatus;
    private boolean canProceedToNext;
    
    // Metadata
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isValidated;
    private String validationNotes;
}