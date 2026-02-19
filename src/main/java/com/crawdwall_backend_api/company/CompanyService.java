package com.crawdwall_backend_api.company;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.crawdwall_backend_api.company.request.CompanyCreateRequest;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.UtilsService;
import com.crawdwall_backend_api.userauthmgt.user.UserService;
import com.crawdwall_backend_api.userauthmgt.user.request.UserCreateRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserCreateResponse;
import com.crawdwall_backend_api.userauthmgt.user.UserType;
import com.crawdwall_backend_api.company.request.CompanyUpdateRequest;
import com.crawdwall_backend_api.company.response.CompanyResponse;
import com.crawdwall_backend_api.company.response.CompanyKyc1ReviewResponse;
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

import com.crawdwall_backend_api.company.companyKycOne.request.CompanyDeclarationConsentCreateRequest;
import com.crawdwall_backend_api.company.response.CompanyAuthResponse;
import com.crawdwall_backend_api.userauthmgt.user.request.UserAuthRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserResponse;
import com.crawdwall_backend_api.utils.exception.UnauthorizedException;
import com.crawdwall_backend_api.utils.appsecurity.JwtService;
import java.util.Map;
import java.util.HashMap;
import com.crawdwall_backend_api.company.request.CompanyBankingAndFinancialAccountsRequest;
import com.crawdwall_backend_api.company.request.CompanyAuthorizedSignatoriesAndControlCreateRequest;
import com.crawdwall_backend_api.company.request.CompanySignatoryRequest;
import com.crawdwall_backend_api.company.request.CompanyFinancialIntegrityAndRiskControlCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyExecutionAndReportingReadinessCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyCapitalGovernanceAgreementCreateRequest;

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
                .firstName(request.companyName())
                .emailAddress(request.companyEmail())
                .password(request.password())
                .userType(UserType.COMPANY)
                .build());
    

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
                .countryOfRegistration(company.getCountryOfRegistration())
                .companyType(company.getCompanyType())
                .otherInformationForCompanyType(company.getOtherInformationForCompanyType())
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


    public CompanyResponse setUpCompanyProfile(String companyId,CompanyProfileSetUpCreateRequest request) {
       
       if(request.companyType() == CompanyType.OTHER) {
        if(request.otherInformationForCompanyType() == null || request.otherInformationForCompanyType().isBlank()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_OTHER_VALUE_REQUIRED);
        }
       }
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        company.setCompanyName(request.companyName());
        company.setCompanyPhone(request.companyPhone());
        company.setCompanyWebsite(request.companyWebsite());
        company.setCompanyType(request.companyType());
        company.setCompanyEstablishedDate(request.establishedDate());
        company.setCompanySocialMediaType(request.companySocialMediaType());
        company.setCompanySocialMediaUrl(request.companySocialMediaUrl());
        company.setCompanyKycOneSteps(updateCompanyKycOneStep(company,CompanyKycOneStep.COMPANY_PROFILE_SETUP));
        company.setCompanyAddress(request.address());
        company.setCountryOfRegistration(request.country());
        company.setOtherInformationForCompanyType(request.otherInformationForCompanyType() != null ? request.otherInformationForCompanyType() : "");
        companyRepository.save(company);
        return buildCompanyResponse(company);
    }

    

    public void setUpCompanyKycOneStep(Company company,CompanyKycOneStep companyKycOneStep) {
        Set<CompanyKycOneStep> companyKycOneSteps = company.getCompanyKycOneSteps();
        if(companyKycOneSteps == null) {
            companyKycOneSteps = new HashSet<>();
        }
       companyKycOneSteps.add(companyKycOneStep);
       company.setCompanyKycOneSteps(companyKycOneSteps);
       companyRepository.save(company);
    }

    private Set<CompanyKycOneStep> updateCompanyKycOneStep(Company company,CompanyKycOneStep companyKycOneStep) {
        Set<CompanyKycOneStep> companyKycOneSteps = company.getCompanyKycOneSteps();
        if(companyKycOneSteps == null) {
            companyKycOneSteps = new HashSet<>();
        }
        companyKycOneSteps.add(companyKycOneStep);
        company.setCompanyKycOneSteps(companyKycOneSteps);
       return companyKycOneSteps;
    }


    public Company getCompany(String companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
    }






    CompanyAuthResponse authenticateCompany(UserAuthRequest request) {
        UserResponse userResponse = userService.authenticateUser(request, UserType.COMPANY);
        log.info("#3 User authenticated - ID: {}", userResponse.userId());

        if (!userResponse.isVerified() && !userResponse.isActive()) {
            log.warn("#4 User verification/activation failed");
            return CompanyAuthResponse.builder().userResponse(userResponse).build();
        }

        log.info("#5 Finding company for user ID: {}", userResponse.userId());
        Company company = companyRepository.findByUserId(userResponse.userId())
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_USER_NOT_FOUND));
        if (company.getStatus() != Status.ACTIVE) {
            log.error("#8 Company status invalid: {}", company.getStatus());
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

    public void setUpBankingAndFinancialAccounts(String companyId, CompanyBankingAndFinancialAccountsRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        if (!company.isKycCompleted()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_LEVEL_ONE_NOT_COMPLETED);
        }
        
        Set<CompanyBankingAndFinancialAccounts> bankingAccounts = buildBankingAccounts(request);
        
        company.setCompanyBankingAndFinancialAccounts(bankingAccounts);
        updateKycTwoProgress(company, CompanyKycTwoStep.BANKING_AND_FINANCIAL_ACCOUNTS);
        
        companyRepository.save(company);
    }

    private Set<CompanyBankingAndFinancialAccounts> buildBankingAccounts(CompanyBankingAndFinancialAccountsRequest request) {
        Set<CompanyBankingAndFinancialAccounts> accounts = new HashSet<>();
        
        CompanyBankingAndFinancialAccounts account = CompanyBankingAndFinancialAccounts.builder()
                .bankName(request.bankName())
                .accountName(request.accountName())
                .accountNumber(request.accountNumber())
                .accountType(request.accountType())
                .accountCurrency(request.accountCurrency())
                .letterOfStatementUrl(request.letterOfStatementUrl())
                .build();
        
        accounts.add(account);
        return accounts;
    }

    private void updateKycTwoProgress(Company company, CompanyKycTwoStep step) {
        Set<CompanyKycTwoStep> steps = company.getCompanyKycTwoSteps();
        if (steps == null) {
            steps = new HashSet<>();
        }
        steps.add(step);
        company.setCompanyKycTwoSteps(steps);
        
        if (company.getKycTwoStartedAt() == null) {
            company.setKycTwoStartedAt(LocalDateTime.now());
        }
    }

    public void setUpAuthorizedSignatoriesAndControl(String companyId, CompanyAuthorizedSignatoriesAndControlCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        if (!company.isKycCompleted()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_LEVEL_ONE_NOT_COMPLETED);
        }
        
        verifyAuthorizedSignatoriesRequest(request);
        
        Set<CompanyAuthorizedSignatoriesAndControl> authorizedSignatories = buildAuthorizedSignatories(request);
        
        company.setCompanyAuthorizedSignatoriesAndControl(authorizedSignatories);
        updateKycTwoProgress(company, CompanyKycTwoStep.AUTHORIZED_SIGNATORIES_AND_CONTROL);
        
        companyRepository.save(company);
    }

    private Set<CompanyAuthorizedSignatoriesAndControl> buildAuthorizedSignatories(CompanyAuthorizedSignatoriesAndControlCreateRequest request) {
        Set<CompanyAuthorizedSignatoriesAndControl> signatories = new HashSet<>();
        
        CompanyAuthorizedSignatoriesAndControl primarySignatory = CompanyAuthorizedSignatoriesAndControl.builder()
                .signatoryName(request.primarySignatory().signatoryName())
                .role(request.primarySignatory().role())
                .email(request.primarySignatory().email())
                .phoneNumber(request.primarySignatory().phoneNumber())
                .isPrimary(true)
                .governmentIdDocumentUrl(request.governmentIdDocumentUrl())
                .authorizationLetterUrl(request.authorizationLetterUrl())
                .build();
        
        signatories.add(primarySignatory);
        
        // Add secondary signatories if present
        if (request.secondarySignatories() != null && !request.secondarySignatories().isEmpty()) {
            for (CompanySignatoryRequest secondaryReq : request.secondarySignatories()) {
                CompanyAuthorizedSignatoriesAndControl secondarySignatory = CompanyAuthorizedSignatoriesAndControl.builder()
                        .signatoryName(secondaryReq.signatoryName())
                        .role(secondaryReq.role())
                        .email(secondaryReq.email())
                        .phoneNumber(secondaryReq.phoneNumber())
                        .isPrimary(false)
                        .build();
                signatories.add(secondarySignatory);
            }
        }
        
        return signatories;
    }

    private void verifyAuthorizedSignatoriesRequest(CompanyAuthorizedSignatoriesAndControlCreateRequest request) {
        if (request.primarySignatory() == null) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_PRIMARY_SIGNATORY_NAME_REQUIRED);
        }
        
        CompanySignatoryRequest primary = request.primarySignatory();
        if (primary.signatoryName() == null || primary.signatoryName().isBlank()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_PRIMARY_SIGNATORY_NAME_REQUIRED);
        }
        if (primary.role() == null || primary.role().isBlank()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_SIGNATORY_ROLE_REQUIRED);
        }
        if (primary.email() == null || primary.email().isBlank()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_SIGNATORY_EMAIL_REQUIRED);
        }
        if (primary.phoneNumber() == null || primary.phoneNumber().isBlank()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_SIGNATORY_PHONE_REQUIRED);
        }
        
        if (request.secondarySignatories() != null && !request.secondarySignatories().isEmpty()) {
            for (CompanySignatoryRequest secondary : request.secondarySignatories()) {
                if (secondary.signatoryName() == null || secondary.signatoryName().isBlank()) {
                    throw new InvalidInputException(ApiResponseMessages.ERROR_PRIMARY_SIGNATORY_NAME_REQUIRED);
                }
                if (secondary.role() == null || secondary.role().isBlank()) {
                    throw new InvalidInputException(ApiResponseMessages.ERROR_SIGNATORY_ROLE_REQUIRED);
                }
                if (secondary.email() == null || secondary.email().isBlank()) {
                    throw new InvalidInputException(ApiResponseMessages.ERROR_SIGNATORY_EMAIL_REQUIRED);
                }
                if (secondary.phoneNumber() == null || secondary.phoneNumber().isBlank()) {
                    throw new InvalidInputException(ApiResponseMessages.ERROR_SIGNATORY_PHONE_REQUIRED);
                }
            }
        }
    }

    public void setUpFinancialIntegrityAndRiskControl(String companyId, CompanyFinancialIntegrityAndRiskControlCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        if (!company.isKycCompleted()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_LEVEL_ONE_NOT_COMPLETED);
        }
        
        verifyFinancialIntegrityRequest(request);
        
        CompanyFinancialIntegrityAndRiskControl financialIntegrity = buildFinancialIntegrity(request);
        
        company.setCompanyFinancialIntegrityAndRiskControl(financialIntegrity);
        updateKycTwoProgress(company, CompanyKycTwoStep.FINANCIAL_INTEGRITY_AND_RISK_CONTROLS);
        
        companyRepository.save(company);
    }

    private CompanyFinancialIntegrityAndRiskControl buildFinancialIntegrity(CompanyFinancialIntegrityAndRiskControlCreateRequest request) {
        return CompanyFinancialIntegrityAndRiskControl.builder()
                .primaryRevenueSources(request.primaryRevenueSources())
                .expectedTransactionVolume(request.expectedTransactionVolume())
                .sourceOfFundsDeclaration(request.sourceOfFundsDeclaration())
                .isPoliticallyExposedPerson(request.isPoliticallyExposedPerson())
                .pepRole(request.pepRole())
                .pepCountry(request.pepCountry())
                .pepYear(request.pepYear())
                .hasLitigationBankruptcyOrInsolvency(request.hasLitigationBankruptcyOrInsolvency())
                .litigationNature(request.litigationNature())
                .litigationYear(request.litigationYear())
                .litigationCurrentStatus(request.litigationCurrentStatus())
                .isSubjectToSanctions(request.isSubjectToSanctions())
                .sanctionsPartyAffected(request.sanctionsPartyAffected())
                .sanctionsNature(request.sanctionsNature())
                .sanctionsCurrentStatus(request.sanctionsCurrentStatus())
                .build();
    }

    private void verifyFinancialIntegrityRequest(CompanyFinancialIntegrityAndRiskControlCreateRequest request) {
        if (request.isPoliticallyExposedPerson()) {
            if (request.pepRole() == null || request.pepRole().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_PEP_ROLE_REQUIRED);
            }
            if (request.pepCountry() == null || request.pepCountry().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_PEP_COUNTRY_REQUIRED);
            }
            if (request.pepYear() == null) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_PEP_YEAR_REQUIRED);
            }
        }
        
        if (request.hasLitigationBankruptcyOrInsolvency()) {
            if (request.litigationNature() == null || request.litigationNature().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_LITIGATION_NATURE_REQUIRED);
            }
            if (request.litigationYear() == null) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_LITIGATION_YEAR_REQUIRED);
            }
            if (request.litigationCurrentStatus() == null || request.litigationCurrentStatus().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_LITIGATION_STATUS_REQUIRED);
            }
        }
        
        if (request.isSubjectToSanctions()) {
            if (request.sanctionsPartyAffected() == null || request.sanctionsPartyAffected().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_SANCTIONS_PARTY_REQUIRED);
            }
            if (request.sanctionsNature() == null || request.sanctionsNature().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_SANCTIONS_NATURE_REQUIRED);
            }
            if (request.sanctionsCurrentStatus() == null || request.sanctionsCurrentStatus().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_SANCTIONS_STATUS_REQUIRED);
            }
        }
    }


  
    public void setUpExecutionAndReportingReadiness(String companyId, CompanyExecutionAndReportingReadinessCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        if (!company.isKycCompleted()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_LEVEL_ONE_NOT_COMPLETED);
        }
        
        verifyExecutionAndReportingReadinessRequest(request);
        
        CompanyExecutionAndReportingReadiness executionReadiness = buildExecutionAndReportingReadiness(request);
        
        company.setCompanyExecutionAndReportingReadiness(executionReadiness);
        updateKycTwoProgress(company, CompanyKycTwoStep.EXECUTION_AND_REPORTING_READINESS);
        
        companyRepository.save(company);
    }

    private CompanyExecutionAndReportingReadiness buildExecutionAndReportingReadiness(CompanyExecutionAndReportingReadinessCreateRequest request) {
        return CompanyExecutionAndReportingReadiness.builder()
                .fullName(request.fullName())
                .role(request.role())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .accountingSystemUsed(request.accountingSystemUsed())
                .financialReportingFrequency(request.financialReportingFrequency())
                .hasPastEscrowUse(request.hasPastEscrowUse())
                .typeOfArrangement(request.typeOfArrangement())
                .purposeOfEscrow(request.purposeOfEscrow())
                .counterpartyPlatformUsed(request.counterpartyPlatformUsed())
                .durationOfAgreement(request.durationOfAgreement())
                .build();
    }

    private void verifyExecutionAndReportingReadinessRequest(CompanyExecutionAndReportingReadinessCreateRequest request) {
        if (request.hasPastEscrowUse()) {
            if (request.typeOfArrangement() == null || request.typeOfArrangement().isEmpty()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_TYPE_OF_ARRANGEMENT_REQUIRED);
            }
            if (request.purposeOfEscrow() == null || request.purposeOfEscrow().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_PURPOSE_OF_ESCROW_REQUIRED);
            }
            if (request.counterpartyPlatformUsed() == null || request.counterpartyPlatformUsed().isBlank()) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_COUNTERPARTY_PLATFORM_REQUIRED);
            }
            if (request.durationOfAgreement() == null) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_DURATION_OF_AGREEMENT_REQUIRED);
            }
        }
    }


    public void setUpCapitalGovernanceAgreement(String companyId, CompanyCapitalGovernanceAgreementCreateRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        if (!company.isKycCompleted()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_LEVEL_ONE_NOT_COMPLETED);
        }
        
        verifyCapitalGovernanceAgreementRequest(request);
        
        CompanyCapitalGovernanceAgreement capitalGovernance = buildCapitalGovernanceAgreement(request);
        
        company.setCompanyCapitalGovernanceAgreement(capitalGovernance);
        updateKycTwoProgress(company, CompanyKycTwoStep.CAPITAL_GOVERNANCE_AGREEMENT);
        
        if (company.getCompanyKycTwoSteps() != null && company.getCompanyKycTwoSteps().size() == 5) {
            company.setKycTwoCompleted(true);
            company.setKycTwoCompletedAt(LocalDateTime.now());
        }
        
        companyRepository.save(company);
    }

    private CompanyCapitalGovernanceAgreement buildCapitalGovernanceAgreement(CompanyCapitalGovernanceAgreementCreateRequest request) {
        return CompanyCapitalGovernanceAgreement.builder()
                .consentToMilestoneBasedDisbursement(request.consentToMilestoneBasedDisbursement())
                .consentToEscrowOrControlledAccount(request.consentToEscrowOrControlledAccount())
                .consentToThirdPartyMonitoring(request.consentToThirdPartyMonitoring())
                .understandSuspensionPolicy(request.understandSuspensionPolicy())
                .digitalSignatureUrl(request.digitalSignatureUrl())
                .agreementDate(request.agreementDate())
                .build();
    }

    private void verifyCapitalGovernanceAgreementRequest(CompanyCapitalGovernanceAgreementCreateRequest request) {
        if (!request.consentToMilestoneBasedDisbursement()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_CONSENT_MILESTONE_DISBURSEMENT_REQUIRED);
        }
        if (!request.consentToEscrowOrControlledAccount()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_CONSENT_ESCROW_ACCOUNT_REQUIRED);
        }
        if (!request.consentToThirdPartyMonitoring()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_CONSENT_THIRD_PARTY_MONITORING_REQUIRED);
        }
        if (!request.understandSuspensionPolicy()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_UNDERSTAND_SUSPENSION_POLICY_REQUIRED);
        }
    }

    public CompanyKyc1ReviewResponse getKyc1ReviewData(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        return CompanyKyc1ReviewResponse.builder()
                .companyName(company.getCompanyName())
                .companyEmail(company.getCompanyEmail())
                .companyPhone(company.getCompanyPhone())
                .companyWebsite(company.getCompanyWebsite())
                .companyType(company.getCompanyType())
                .companyEstablishedDate(company.getCompanyEstablishedDate())
                .companySocialMediaType(company.getCompanySocialMediaType())
                .companySocialMediaUrl(company.getCompanySocialMediaUrl())
                .companyAddress(company.getCompanyAddress())

                .build();
    }

    public void submitKyc1(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));

        if (company.getCompanyKycOneSteps() == null || company.getCompanyKycOneSteps().size() < 5) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_LEVEL_ONE_NOT_COMPLETED);
        }

        company.setKycCompleted(true);
        company.setKycCompletedAt(LocalDateTime.now());

        companyRepository.save(company);
    }

    public CompanyResponse getCompanyProfileDetails(String companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        return buildCompanyResponse(company);
    }


    public void updateCompany(Company company) {
        companyRepository.save(company);
    }



  
}
