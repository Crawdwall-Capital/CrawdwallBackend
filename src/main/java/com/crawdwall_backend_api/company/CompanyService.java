package com.crawdwall_backend_api.company;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.crawdwall_backend_api.company.request.CompanyCreateRequest;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.company.Company;
import com.crawdwall_backend_api.company.CompanyRepository;
import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.utils.Address;
import com.crawdwall_backend_api.utils.UtilsService;
import com.crawdwall_backend_api.userauthmgt.user.UserService;
import com.crawdwall_backend_api.userauthmgt.user.request.UserCreateRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserCreateResponse;
import com.crawdwall_backend_api.userauthmgt.user.UserType;
import com.crawdwall_backend_api.company.request.CompanyUpdateRequest;
import com.crawdwall_backend_api.company.response.CompanyResponse;
import com.crawdwall_backend_api.utils.PaginatedData;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.util.StringUtils;
import com.crawdwall_backend_api.utils.Status;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import org.springframework.data.domain.PageImpl;
import com.crawdwall_backend_api.userauthmgt.user.User;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.UserResetPasswordRequest;
import java.time.LocalDateTime;
import com.crawdwall_backend_api.utils.exception.ResourceNotFoundException;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;

import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.company.request.CompanyProfileSetUpCreateRequest;
import java.util.Set;
import java.util.HashSet;
import com.crawdwall_backend_api.company.request.CompanyLeaderShipOwnerShipSetUpRequest;
import com.crawdwall_backend_api.company.request.CompanyLeaderShipOwnerShipCreateRequest;
import com.crawdwall_backend_api.company.CompanyKycOneStep;
import com.crawdwall_backend_api.company.request.DocumentEntityValueCreateRequestSetUp;
import com.crawdwall_backend_api.company.request.DocumentEntityValueCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyDeclarationConsentCreateRequest;
import com.crawdwall_backend_api.company.CompanyDeclarationConsent;
import com.crawdwall_backend_api.company.response.CompanyAuthResponse;
import com.crawdwall_backend_api.userauthmgt.user.request.UserAuthRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserResponse;
import com.crawdwall_backend_api.utils.exception.UnauthorizedException;
import com.crawdwall_backend_api.utils.appsecurity.JwtService;
import java.util.Map;
import java.util.HashMap;

@RequiredArgsConstructor
@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UtilsService utilsService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final JwtService jwtService;
    
    public void createCompany(CompanyCreateRequest request) {
      
        
        // Check if company name already exists
        if (companyRepository.existsByCompanyName(request.companyName())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NAME_ALREADY_EXISTS);
        }
        
        // Check if company email already exists
        if (companyRepository.existsByCompanyEmail(request.companyEmail())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_EMAIL_ALREADY_EXISTS);
        }

        // Check if Terms and condition are accepted
        if (!request.acceptTermsAndConditions()) {
        throw new InvalidInputException(ApiResponseMessages.ERROR_TERMS_AND_CONDITION_ERROR);
}
         
         
        // Create user account for the company
        UserCreateResponse userCreateResponse = userService.createUser(UserCreateRequest.builder()
                .firstName(request.companyName()) // Use company name as first name
                .lastName("Company") // Default last name
                .emailAddress(request.companyEmail())
                .password(request.password())
                .userType(UserType.COMPANY)
                .build());
    
        // Create company entity with minimal required data
        Company company = buildCompany(request, userCreateResponse.userId());
        companyRepository.save(company);
    }


    public void updateCompany(CompanyUpdateRequest request, String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
    
        // Validate unique fields if changed
        validateUniqueFields(company, request);
    
        // Update fields if provided and different
        updateIfChanged(company, request);
        
        companyRepository.save(company);
    }
    
    private void validateUniqueFields(Company existing, CompanyUpdateRequest request) {
        // Company Name
        if (request.companyName() != null && !existing.getCompanyName().equals(request.companyName())) {
            if (companyRepository.existsByCompanyName(request.companyName())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NAME_ALREADY_EXISTS);
            }
        }
        
      
        
        // Company Phone
        if (request.companyPhone() != null && !existing.getCompanyPhone().equals(request.companyPhone())) {
            if (companyRepository.existsByCompanyPhone(request.companyPhone())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_PHONE_ALREADY_EXISTS);
            }
        }
        
        
    }
    
    private void updateIfChanged(Company company, CompanyUpdateRequest request) {
        if (request.companyName() != null) {
            company.setCompanyName(request.companyName());
        }
        if (request.companyPhone() != null) {
            company.setCompanyPhone(request.companyPhone());
        }
        if (request.companyWebsite() != null) {
            company.setCompanyWebsite(request.companyWebsite());
        }
        if (request.companyEstablishedDate() != null) {
            company.setCompanyEstablishedDate(request.companyEstablishedDate());
        }
        if (request.companyType() != null) {
            company.setCompanyType(request.companyType());
        }
        if (request.companySocialMediaType() != null) {
            company.setCompanySocialMediaType(request.companySocialMediaType());
        }
        if (request.companySocialMediaUrl() != null) {
            company.setCompanySocialMediaUrl(request.companySocialMediaUrl());
        }
     
    }

    /**
         * Builds search criteria for nominees using MongoDB queries.
         *
         * @param searchParam search keyword
         * @param page        page number
         * @param size        page size
         * @return paginated nominee results
         */
    private Page<Company> searchCompaniesWithCriteria(String searchParam, int page, int size) {
        PageRequest pageRequest = PageRequest.of(Math.max(0, page - 1), size,
                Sort.by(Sort.Order.desc("createdAt")));

        Query query = new Query();

        query.addCriteria(Criteria.where("status")
                .in(Status.PENDING, Status.ACTIVE, Status.INACTIVE, Status.BLOCKED));

        if (StringUtils.hasText(searchParam)) {
            String trimmed = searchParam.trim();
            List<Criteria> ors = buildSearchCriteria(trimmed);
            query.addCriteria(new Criteria().orOperator(ors.toArray(new Criteria[0])));
        }

        long total = mongoTemplate.count(query, Company.class);
        query.with(pageRequest);
        List<Company> companies = mongoTemplate.find(query, Company.class);

        return new PageImpl<>(companies, pageRequest, total);
    }

    private CompanyResponse buildCompanyResponse(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .companyEmail(company.getCompanyEmail())
                .companyPhone(company.getCompanyPhone())
                .companyWebsite(company.getCompanyWebsite())
                .companyLogo(company.getCompanyLogo())
                .companyRegistrationNumber(company.getCompanyRegistrationNumber())
                .companyEstablishedDate(company.getCompanyEstablishedDate())
                .companyType(company.getCompanyType())
                .userId(company.getUserId())
                .companySocialMediaType(company.getCompanySocialMediaType())
                .companySocialMediaUrl(company.getCompanySocialMediaUrl())
                .isActive(company.isActive())
                .isDeleted(company.isDeleted())
                .isVerified(company.isVerified())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .companyAddress(company.getCompanyAddress())
                .status(company.getStatus())
                .build();
    }

    /**
         * Builds search criteria supporting name matching patterns.
         *
         * @param searchParam input keyword
         * @return list of OR criteria
         */
    private List<Criteria> buildSearchCriteria(String searchParam) {
        List<Criteria> ors = new ArrayList<>();
        ors.add(Criteria.where("companyName").regex(searchParam, "i"));
        ors.add(Criteria.where("companyEmail").regex(searchParam, "i"));
        ors.add(Criteria.where("companyPhone").regex(searchParam, "i"));
        ors.add(Criteria.where("companyRegistrationNumber").regex(searchParam, "i"));

        String[] parts = searchParam.split("\s+");
        if (parts.length == 2) {
            ors.add(new Criteria().andOperator(
                    Criteria.where("companyName").regex(parts[0], "i"),
                    Criteria.where("companyRegistrationNumber").regex(parts[1], "i")));

            ors.add(new Criteria().andOperator(
                    Criteria.where("companyName").regex(parts[1], "i"),
                    Criteria.where("companyRegistrationNumber").regex(parts[0], "i")));
        }
        return ors;
    }

    public CompanyResponse getCompanyById(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        return buildCompanyResponse(company);
    }

    public CompanyResponse getCompanyByUserId(String userId) {
        Company company = companyRepository.findByUserId(userId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        return buildCompanyResponse(company);
    }

    public PaginatedData getAllCompanies(int page, int size, String searchParam) {
        Page<Company> companies = searchCompaniesWithCriteria(searchParam, page, size);
        List<CompanyResponse> companyResponses = companies.getContent()
                .stream()
                .map(this::buildCompanyResponse)
                .collect(Collectors.toList());
        return PaginatedData.builder()
                .totalPage(companies.getTotalPages())
                .numberOfElements(companies.getNumberOfElements())
                .totalElements(companies.getTotalElements())
                .numberOfElements(companies.getNumberOfElements())
                .totalElements(companies.getTotalElements())
                .data(companyResponses)
                .build();
    }

    private Company buildCompany(CompanyCreateRequest request, String userId) {
        return Company.builder()
                .companyName(request.companyName())
                .companyEmail(request.companyEmail())
                .userId(userId)
                .isActive(false) // Initially inactive until verified
                .isDeleted(false)
                .isVerified(false)
                .status(Status.PENDING)
                .build();
    }

    public void deleteCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        companyRepository.delete(company);
    }

    public void activateCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setStatus(Status.ACTIVE);
        companyRepository.save(company);
    }
    
    public void deactivateCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setStatus(Status.INACTIVE);
        companyRepository.save(company);
    }
    
    
    
    public void blockCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setStatus(Status.BLOCKED);
        companyRepository.save(company);
    }
    
    public void unblockCompany(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setStatus(Status.ACTIVE);
        companyRepository.save(company);
    }

  
    public void changeCompanyPassword(String userId, PasswordChangeRequest request) {
        userService.changeUserPassword(userId, UserType.ADMIN, request);
    }

    public void initiateResetPassword(String emailAddress) {
        userService.initiateResetPassword(emailAddress, UserType.COMPANY);
    }

    public void verifyOtp(UserVerifyOtpRequest request) {
    User user =    userService.verifyOtp(request);
    Company company = companyRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND, true, false));
    company.setVerified(true);
    company.setStatus(Status.ACTIVE);
    company.setVerifiedAt(LocalDateTime.now());
    company.setActive(true);
    companyRepository.save(company);
    }

    public void resendOtp(String emailAddress, UserOtpType userOtpType) {
        userService.resendOtp(emailAddress, userOtpType);
    }

    public void resetPassword(UserResetPasswordRequest request) {
        userService.resetPassword(request);
    }


    public void setUpCompanyProfile(String companyId,CompanyProfileSetUpCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setCompanyName(request.companyName());
        company.setCompanyPhone(request.companyPhone());
        company.setCompanyWebsite(request.companyWebsite());
        company.setCompanyType(request.companyType());
        company.setCompanyEstablishedDate(request.establishedDate());
        company.setCompanySocialMediaType(request.companySocialMediaType());
        company.setCompanySocialMediaUrl(request.companySocialMediaUrl());
        company.setCompanyKycOneSteps(setUpCompanyKycOneStep(company,CompanyKycOneStep.COMPANY_PROFILE_SETUP));
        company.setCompanyAddress(request.address());
        companyRepository.save(company);
    }

    

    private Set<CompanyKycOneStep> setUpCompanyKycOneStep(Company company,CompanyKycOneStep companyKycOneStep) {
        Set<CompanyKycOneStep> companyKycOneSteps = company.getCompanyKycOneSteps();
        if(companyKycOneSteps == null) {
            companyKycOneSteps = new HashSet<>();
        }
       companyKycOneSteps.add(companyKycOneStep);
       return companyKycOneSteps;
    }

    public void setUpCompanyLeaderShipOwnerShip(String companyId,CompanyLeaderShipOwnerShipCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        verifyCompanyLeaderShipOwnerShipRequest(request.getCompanyLeaderShipOwnerShipSetUpRequest());
        Set<CompanyLeaderShipOwnerShip> companyLeaderShipOwnerShip = buildCompanyLeaderShipOwnerShip(request.getCompanyLeaderShipOwnerShipSetUpRequest());
        company.setCompanyLeaderShipOwnerShip(companyLeaderShipOwnerShip);
        company.setCompanyKycOneSteps(setUpCompanyKycOneStep(company,CompanyKycOneStep.LEADER_AND_OWNERSHIP));
        companyRepository.save(company);
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

    public void setUpCompanyTrackRecordCredibility(String companyId,CompanyTrackRecordCredibilityCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        verifyCompanyTrackRecordCredibilityRequest(request);
        CompanyTrackRecordCredibility companyTrackRecordCredibility = buildCompanyTrackRecordCredibility(request);
        company.setCompanyTrackRecordCredibility(companyTrackRecordCredibility);
        company.setCompanyKycOneSteps(setUpCompanyKycOneStep(company,CompanyKycOneStep.TRACK_RECORDS_AND_CREDIBILITY));
        companyRepository.save(company);
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

    public void setUpCompanyComplianceAndIdentity(String companyId,DocumentEntityValueCreateRequestSetUp request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setTaxIdentificationDocument(buildDocumentEntityValue(request.getTaxIdentificationDocumentCreateRequest()));
        company.setProofOfAddressDocument(buildDocumentEntityValue(request.getProofOfAddressDocumentCreateRequest()));
        company.setGovernmentIdDocument(buildDocumentEntityValue(request.getGovernmentIdDocumentCreateRequest()));
        company.setComplianceAndIdentityDocument(buildDocumentEntityValue(request.getComplianceAndIdentityDocumentCreateRequest()));
        company.setCompanyKycOneSteps(setUpCompanyKycOneStep(company,CompanyKycOneStep.COMPLIANCE_AND_IDENTITY));
        companyRepository.save(company);
    }

    private DocumentEntityValue buildDocumentEntityValue(DocumentEntityValueCreateRequest request) {
        return DocumentEntityValue.builder()
                .documentEntityValueType(request.getDocumentEntityValueType())
                .documentEntityValueUrl(request.getDocumentEntityValueUrl())
                .submittedAt(LocalDateTime.now())
                .build();
    }

    public void setUpCompanyDeclarationConsent(String companyId,CompanyDeclarationConsentCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setCompanyDeclarationConsent(buildCompanyDeclarationConsent(request));
        company.setCompanyKycOneSteps(setUpCompanyKycOneStep(company,CompanyKycOneStep.DECLARATION_AND_CONSENT));
        companyRepository.save(company);
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


    CompanyAuthResponse authenticateCompany(UserAuthRequest request) {
        UserResponse userResponse = userService.authenticateUser(request, UserType.COMPANY);
        if (!userResponse.isVerified() && !userResponse.isActive()){
            return CompanyAuthResponse.builder().userResponse(userResponse).build();
        }
        Company company = companyRepository.findByUserId(userResponse.userId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));
        if (company.getStatus() != Status.ACTIVE) {
            throw new UnauthorizedException(ApiResponseMessages.ERROR_COMPANY_APP_ACCESS_DISABLED);
        }
        return CompanyAuthResponse.builder()
                .token(generateJwtForCompany(company, userResponse))
                .companyId(company.getId()).userId(userResponse.userId())
                .refreshToken(generateRefreshToken(company, userResponse))
                .kycCompleted(company.isKycCompleted())
                .documentVerified(company.isDocumentVerified())
                .userResponse(userResponse).build();

    }

     /**
         * Generates a refresh JWT token for a company.
         *
         * @param company       company entity
         * @param userResponse  user information
         * @return signed refresh token
         */
     private String generateRefreshToken(Company company, UserResponse userResponse) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("companyId", company.getId());
        extraClaims.put("userId", userResponse.userId());
        extraClaims.put("email", userResponse.emailAddress());
        extraClaims.put("userType", userResponse.userType());
        return jwtService.generateRefreshToken(userResponse.emailAddress(), extraClaims);
    }

    /**
     * Generates an authentication JWT token for a company.
     *
     * @param company      company entity
     * @param userResponse user details
     * @return JWT token
     */
    private String generateJwtForCompany(Company company, UserResponse userResponse) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("companyId", company.getId());
        extraClaims.put("userId", userResponse.userId());
        extraClaims.put("email", userResponse.emailAddress());
        extraClaims.put("userType", userResponse.userType());
        return jwtService.generateToken(extraClaims, userResponse.emailAddress());
    }




}