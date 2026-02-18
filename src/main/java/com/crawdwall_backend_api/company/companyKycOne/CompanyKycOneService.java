package com.crawdwall_backend_api.company.companyKycOne;

import com.crawdwall_backend_api.company.DocumentEntityValue;
import com.crawdwall_backend_api.company.companyKycOne.request.*;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.crawdwall_backend_api.company.CompanyService;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.exception.ResourceNotFoundException;
import com.crawdwall_backend_api.utils.ApiResponseMessages;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.crawdwall_backend_api.company.CompanyKycOneStep;
import com.crawdwall_backend_api.company.Company;
import com.crawdwall_backend_api.company.CompanyDeclarationConsent;
import com.crawdwall_backend_api.company.response.CompanyKycLevel1DetailsResponse;
import com.crawdwall_backend_api.company.response.CompanyKycLevel1DetailsResponse.CompanyProfileSection;
import com.crawdwall_backend_api.company.response.CompanyKycLevel1DetailsResponse.ComplianceIdentitySection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyKycOneService {

    private final CompanyKycOneRepository companyKycOneRepository;
    private final CompanyService companyService;


    
    public void setUpCompanyLeaderShipOwnerShip(String companyId, CompanyLeaderShipOwnerShipCreateRequest request) {
        Company company = companyService.getCompany(companyId);
        CompanyKycOne companyKycOne = getFreshCompanyKycOne(companyId);
        verifyCompanyLeaderShipOwnerShipRequest(request.getCompanyLeaderShipOwnerShipSetUpRequest());
        Set<CompanyLeaderShipOwnerShip> companyLeaderShipOwnerShip = buildCompanyLeaderShipOwnerShip(request.getCompanyLeaderShipOwnerShipSetUpRequest());
        companyKycOne.setCompanyLeaderShipOwnerShip(companyLeaderShipOwnerShip);
        companyService.setUpCompanyKycOneStep(company,CompanyKycOneStep.LEADER_AND_OWNERSHIP);
        companyKycOneRepository.save(companyKycOne);
    }


    public void setUpCompanyTrackRecordCredibility(String companyId, CompanyTrackRecordCredibilityCreateRequest request) {
        Company company = companyService.getCompany(companyId);
        verifyCompanyTrackRecordCredibilityRequest(request);
        CompanyTrackRecordCredibility companyTrackRecordCredibility = buildCompanyTrackRecordCredibility(request);
        CompanyKycOne companyKycOne = getFreshCompanyKycOne(companyId);
        companyKycOne.setCompanyTrackRecordCredibility(companyTrackRecordCredibility);
        companyService.setUpCompanyKycOneStep(company,CompanyKycOneStep.TRACK_RECORDS_AND_CREDIBILITY);
        companyKycOneRepository.save(companyKycOne);
    }

    public void setUpCompanyComplianceAndIdentity(String companyId, DocumentEntityValueCreateRequestSetUp request) {
        Company company = companyService.getCompany(companyId);
        CompanyKycOne companyKycOne = getFreshCompanyKycOne(companyId);
        companyKycOne.setTaxIdentificationDocument(buildDocumentEntityValue(request.getTaxIdentificationDocumentCreateRequest()));
        companyKycOne.setProofOfAddressDocument(buildDocumentEntityValue(request.getProofOfAddressDocumentCreateRequest()));
        companyKycOne.setCertificateOfIncorporation(buildDocumentEntityValue(request.getCertificateOfIncorporationCreateRequest()));
        companyKycOne.setGovernmentIdDocument(buildDocumentEntityValue(request.getGovernmentIdDocumentCreateRequest()));
        companyKycOne.setComplianceAndIdentityDocument(buildDocumentEntityValue(request.getComplianceAndIdentityDocumentCreateRequest()));
        companyService.setUpCompanyKycOneStep(company,CompanyKycOneStep.COMPLIANCE_AND_IDENTITY);
        companyKycOneRepository.save(companyKycOne);
    }
    public void setUpCompanyDeclarationConsent(String companyId,CompanyDeclarationConsentCreateRequest request) {
        Company company = companyService.getCompany(companyId);
        CompanyKycOne companyKycOne = getFreshCompanyKycOne(companyId);
        verifyCompanyDeclarationConsentRequest(companyKycOne, company);
        CompanyDeclarationConsent companyDeclarationConsent = buildCompanyDeclarationConsent(request);
        companyKycOne.setCompanyDeclarationConsent(companyDeclarationConsent);
        companyService.setUpCompanyKycOneStep(company,CompanyKycOneStep.DECLARATION_AND_CONSENT);
        companyKycOneRepository.save(companyKycOne);

    }
    

    public void completeKyc1(String companyId) {
        Company company = companyService.getCompany(companyId);
        CompanyKycOne companyKycOne = companyKycOneRepository.findByCompanyId(companyId);
        if(companyKycOne == null) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_NOT_STARTED);
        }
        companyKycOne.setKycComplete(true);
        companyKycOne.setKycCompletedAt(LocalDateTime.now());
        companyKycOne.setKycApprovalStage(KycApprovalStage.PENDING);
        companyKycOneRepository.save(companyKycOne);

        companyService.setUpCompanyKycOneStep(company, CompanyKycOneStep.KYC_COMPLETED);
        // will be notifying the admin for the KYC completion
        // will be updating the company status to KYC_COMPLETED
        
    }

   
   


    
    private CompanyDeclarationConsent buildCompanyDeclarationConsent(CompanyDeclarationConsentCreateRequest request) {
        return CompanyDeclarationConsent.builder()
                .agreedToCrawdwallTermsAndConditions(request.isAgreedToCrawdwallTermsAndConditions())
                .confirmedAllSubmittedInformationAreCorrect(request.isConfirmedAllSubmittedInformationAreCorrect())
                .authorizedCrawdwallToPerformBackgroundChecks(request.isAuthorizedCrawdwallToPerformBackgroundChecks())
                .declarationConsentSignatureUrl(request.getDeclarationConsentSignatureUrl())
                .declarationConsentSignedAt(LocalDateTime.now())
                .build();
    }

    private void verifyCompanyDeclarationConsentRequest(CompanyKycOne companyKycOne, Company company) {

        Set<CompanyKycOneStep> companyKycOneSteps = company.getCompanyKycOneSteps();
        if(companyKycOneSteps == null || companyKycOneSteps.isEmpty()) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_NOT_STARTED);
        }

        if(!companyKycOneSteps.contains(CompanyKycOneStep.COMPANY_PROFILE_SETUP)) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }
        Set<CompanyLeaderShipOwnerShip> companyLeaderShipOwnerShip = companyKycOne.getCompanyLeaderShipOwnerShip();
        if (companyLeaderShipOwnerShip.isEmpty()){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }

        CompanyTrackRecordCredibility companyTrackRecordCredibility = companyKycOne.getCompanyTrackRecordCredibility();
        if(companyTrackRecordCredibility == null){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }

        DocumentEntityValue taxIdentificationDocument = companyKycOne.getTaxIdentificationDocument();
        if(taxIdentificationDocument == null){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }
        DocumentEntityValue certificateOfIncorporation = companyKycOne.getCertificateOfIncorporation();
        if(certificateOfIncorporation == null){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }
        DocumentEntityValue proofOfAddressDocument = companyKycOne.getProofOfAddressDocument();
        if(proofOfAddressDocument == null){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }
        DocumentEntityValue governmentIdDocument = companyKycOne.getGovernmentIdDocument();
        if(governmentIdDocument == null){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }
        DocumentEntityValue complianceAndIdentityDocument = companyKycOne.getComplianceAndIdentityDocument();
        if(complianceAndIdentityDocument == null){
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_COMPANY_PROFILE_SETUP_NOT_COMPLETED);
        }


       
    }


    private DocumentEntityValue buildDocumentEntityValue(DocumentEntityValueCreateRequest request) {
        return DocumentEntityValue.builder()
                .documentEntityValueType(request.getDocumentEntityValueType())
                .documentEntityValueUrl(request.getDocumentEntityValueUrl())
                .submittedAt(LocalDateTime.now())
                .build();
    }

    private void verifyCompanyTrackRecordCredibilityRequest(CompanyTrackRecordCredibilityCreateRequest request) {
        if(request.getMajorProjectsDeliveredSocialMediaLinks() == null || request.getMajorProjectsDeliveredSocialMediaLinks().isEmpty()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_TRACK_RECORD_CREDIBILITY_MAJOR_PROJECTS_DELIVERED_SOCIAL_MEDIA_LINKS_IS_REQUIRED);
        }

    }

    private CompanyTrackRecordCredibility buildCompanyTrackRecordCredibility(CompanyTrackRecordCredibilityCreateRequest request) {
        return CompanyTrackRecordCredibility.builder()
                .trackRecordCredibilityType(request.getTrackRecordCredibilityType())
                .numberOfMajorProjectsDelivered(request.getNumberOfMajorProjectsDelivered())
                .yearsOfExperience(request.getYearsOfExperience())
                .majorProjectsDeliveredSocialMediaLinks(request.getMajorProjectsDeliveredSocialMediaLinks())
                .averageProjectExcutedAmount(request.getAverageProjectExcutedAmount())
                .majorSponsorPartnerNames(request.getMajorSponsorPartnerNames())
                .build();
    }


    private Set<CompanyLeaderShipOwnerShip> buildCompanyLeaderShipOwnerShip(Set<CompanyLeaderShipOwnerShipSetUpRequest> request) {
            return request.stream().map(companyLeaderShipOwnerShipSetUpRequest -> CompanyLeaderShipOwnerShip.builder()
                .companyAdminName(companyLeaderShipOwnerShipSetUpRequest.companyAdminName())
                .companyAdminEmail(companyLeaderShipOwnerShipSetUpRequest.companyAdminEmail())
                .companyAdminPhone(companyLeaderShipOwnerShipSetUpRequest.companyAdminPhone())
                .companyAdminNationality(companyLeaderShipOwnerShipSetUpRequest.companyAdminNationality())
                .companyAdminRole(companyLeaderShipOwnerShipSetUpRequest.companyAdminRole())
                .levelOfControl(companyLeaderShipOwnerShipSetUpRequest.levelOfControl())
                .isMainFounder(companyLeaderShipOwnerShipSetUpRequest.isMainFounder())
                .build()).collect(Collectors.toSet());
        }

    private void verifyCompanyLeaderShipOwnerShipRequest(Set<CompanyLeaderShipOwnerShipSetUpRequest> request) {
        if(request == null || request.isEmpty()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_REQUEST_IS_EMPTY);
        }
        boolean hasMainFounder = request.stream().anyMatch(CompanyLeaderShipOwnerShipSetUpRequest::isMainFounder);
        if (!hasMainFounder) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_DOES_NOT_HAVE_MAIN_FOUNDER);
        }

        long mainFounderCount = request.stream().filter(CompanyLeaderShipOwnerShipSetUpRequest::isMainFounder).count();
        if (mainFounderCount > 1) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_HAS_MULTIPLE_MAIN_FOUNDERS);
        }

        List<String> adminEmails = request.stream().map(CompanyLeaderShipOwnerShipSetUpRequest::companyAdminEmail).collect(Collectors.toList());
        if (adminEmails.stream().distinct().count() != adminEmails.size()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_HAS_DUPLICATE_ADMIN_EMAILS);
        }

        List<String> adminPhones = request.stream().map(CompanyLeaderShipOwnerShipSetUpRequest::companyAdminPhone).collect(Collectors.toList());
        if (adminPhones.stream().distinct().count() != adminPhones.size()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_LEADER_SHIP_OWNERSHIP_HAS_DUPLICATE_ADMIN_PHONES);
        }
    }

    private CompanyKycOne getFreshCompanyKycOne(String companyId) {
        CompanyKycOne companyKycOne = companyKycOneRepository.findByCompanyId(companyId);
        if(companyKycOne != null) {
            return companyKycOne;
        }
        return CompanyKycOne.builder()
                .companyId(companyId)
                .kycComplete(false)
                .kycStartedAt(LocalDateTime.now())
                .build();
    }


    // KYC Level 1 - Section 1: Company Profile
    public CompanyProfileSection getCompanyProfile(String companyId) {
        Company company = companyService.getCompany(companyId);

        return CompanyProfileSection.builder()
                .companyName(company.getCompanyName())
                .companyType(company.getCompanyType())
                .countryOfRegistration(company.getCompanyAddress() != null ? 
                    company.getCompanyAddress().country() : null)
                .dateEstablished(company.getCompanyEstablishedDate())
                .streetAddress(company.getCompanyAddress() != null ? 
                    company.getCompanyAddress().streetAddress() : null)
                .city(company.getCompanyAddress() != null ? 
                    company.getCompanyAddress().city() : null)
                .country(company.getCompanyAddress() != null ? 
                    company.getCompanyAddress().country() : null)
                .website(company.getCompanyWebsite())
                .socialMediaLink(company.getCompanySocialMediaUrl())
                .socialMediaType(company.getCompanySocialMediaType())
                .companyEmail(company.getCompanyEmail())
                .phoneNumber(company.getCompanyPhone())
                .build();
    }

    // KYC Level 1 - Section 2: Leadership & Ownership
    public Set<CompanyLeaderShipOwnerShip> getLeadershipOwnership(String companyId) {
        CompanyKycOne kycOne = companyKycOneRepository.findByCompanyId(companyId);
        
        if (kycOne == null) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_NOT_STARTED);
        }

        return kycOne.getCompanyLeaderShipOwnerShip();
    }

    // KYC Level 1 - Section 3: Track Record & Credibility
    public CompanyTrackRecordCredibility getTrackRecord(String companyId) {
        CompanyKycOne kycOne = companyKycOneRepository.findByCompanyId(companyId);
        
        if (kycOne == null) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_NOT_STARTED);
        }

        return kycOne.getCompanyTrackRecordCredibility();
    }

    // KYC Level 1 - Section 4: Compliance & Identity (Documents)
    public ComplianceIdentitySection getComplianceIdentity(String companyId) {
        CompanyKycOne kycOne = companyKycOneRepository.findByCompanyId(companyId);
        
        if (kycOne == null) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_NOT_STARTED);
        }

        return ComplianceIdentitySection.builder()
                .taxIdentificationDocument(kycOne.getTaxIdentificationDocument())
                .certificateOfIncorporation(kycOne.getCertificateOfIncorporation())
                .proofOfAddressDocument(kycOne.getProofOfAddressDocument())
                .governmentIdDocument(kycOne.getGovernmentIdDocument())
                .complianceAndIdentityDocument(kycOne.getComplianceAndIdentityDocument())
                .build();
    }

    // KYC Level 1 - Section 5: Declarations & Consent
    public CompanyDeclarationConsent getDeclarationsConsent(String companyId) {
        CompanyKycOne kycOne = companyKycOneRepository.findByCompanyId(companyId);
        
        if (kycOne == null) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_KYC_ONE_NOT_STARTED);
        }

        return kycOne.getCompanyDeclarationConsent();
    }

}

