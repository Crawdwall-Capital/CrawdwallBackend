package com.crawdwall_backend_api.company.companyKycVerification;

import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.utils.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "company_profile_kyc")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyProfileKyc extends BaseEntity {
    
    @Indexed
    private String companyId; // Reference to main Company entity
    
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
    private KycStepStatus stepStatus; // DRAFT, COMPLETED, SUBMITTED
    private boolean canProceedToNext;
    
    // Validation flags
    private boolean isValidated;
    private String validationNotes;
}