package com.crawdwall_backend_api.company;

import com.crawdwall_backend_api.utils.BaseEntity;
import com.crawdwall_backend_api.utils.Address;
import org.springframework.data.mongodb.core.mapping.Document;
import com.crawdwall_backend_api.utils.Status;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Set;
@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "companies")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company extends BaseEntity {

private   String companyName;
@Indexed(unique = true)
private   String companyEmail;
@Indexed(unique = true)
private   String companyPhone;
private   String companyWebsite;
private   String companyLogo;
@Indexed(unique = true)
private   String companyRegistrationNumber;
private   LocalDate companyEstablishedDate;
private   CompanyType companyType;
private   Address companyAddress;
private   boolean isActive;
private   boolean isDeleted;
private   boolean isVerified;
private   LocalDateTime verifiedAt;
private   String userId;
private   Status status;
private   boolean onboardingAgreedTermsAndConditions;
private  LocalDateTime agreedTermsAndConditionAt; 
private   CompanySocialMediaType companySocialMediaType;
private   String companySocialMediaUrl;
private   Set<CompanyKycOneStep> companyKycOneSteps;




private   boolean isKycCompleted;
private   boolean isDocumentVerified;
private   LocalDateTime kycCompletedAt;
private   LocalDateTime documentVerifiedAt;
private   LocalDateTime kycStartedAt;
private   LocalDateTime documentStartedAt;


private   Set<CompanyKycTwoStep> companyKycTwoSteps;
private   Set<CompanyBankingAndFinancialAccounts> companyBankingAndFinancialAccounts;
private   Set<CompanyAuthorizedSignatoriesAndControl> companyAuthorizedSignatoriesAndControl;
private   CompanyFinancialIntegrityAndRiskControl companyFinancialIntegrityAndRiskControl;
private   CompanyExecutionAndReportingReadiness companyExecutionAndReportingReadiness;
private   CompanyCapitalGovernanceAgreement companyCapitalGovernanceAgreement;
private   boolean isKycTwoCompleted;
private   LocalDateTime kycTwoCompletedAt;
private   LocalDateTime kycTwoStartedAt;
private   String countryOfRegistration;
private   String otherInformationForCompanyType;
    }
