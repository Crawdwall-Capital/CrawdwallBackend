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

@RequiredArgsConstructor
@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UtilsService utilsService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    
    public void createCompany(CompanyCreateRequest request) {
        // Validate password confirmation
        if (!request.password().equals(request.confirmPassword())) {
            throw new InvalidInputException("Password and confirm password do not match");
        }
        
        // Check if company name already exists
        if (companyRepository.existsByCompanyName(request.companyName())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NAME_ALREADY_EXISTS);
        }
        
        // Check if company email already exists
        if (companyRepository.existsByCompanyEmail(request.companyEmail())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_EMAIL_ALREADY_EXISTS);
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
        
        // Company Email
        if (request.companyEmail() != null && !existing.getCompanyEmail().equals(request.companyEmail())) {
            if (companyRepository.existsByCompanyEmail(request.companyEmail())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_EMAIL_ALREADY_EXISTS);
            }
        }
        
        // Company Phone
        if (request.companyPhone() != null && !existing.getCompanyPhone().equals(request.companyPhone())) {
            if (companyRepository.existsByCompanyPhone(request.companyPhone())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_PHONE_ALREADY_EXISTS);
            }
        }
        
        // Company Registration Number
        if (request.companyRegistrationNumber() != null && 
            !existing.getCompanyRegistrationNumber().equals(request.companyRegistrationNumber())) {
            if (companyRepository.existsByCompanyRegistrationNumber(request.companyRegistrationNumber())) {
                throw new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_REGISTRATION_NUMBER_ALREADY_EXISTS);
            }
        }
    }
    
    private void updateIfChanged(Company company, CompanyUpdateRequest request) {
        if (request.companyName() != null) {
            company.setCompanyName(request.companyName());
        }
        if (request.companyEmail() != null) {
            company.setCompanyEmail(request.companyEmail());
        }
        if (request.companyPhone() != null) {
            company.setCompanyPhone(request.companyPhone());
        }
        if (request.companyWebsite() != null) {
            company.setCompanyWebsite(request.companyWebsite());
        }
        if (request.companyLogo() != null) {
            company.setCompanyLogo(request.companyLogo());
        }
        if (request.companyRegistrationNumber() != null) {
            company.setCompanyRegistrationNumber(request.companyRegistrationNumber());
        }
        if (request.companyRegistrationDate() != null) {
            company.setCompanyRegistrationDate(request.companyRegistrationDate());
        }
        if (request.companyType() != null) {
            company.setCompanyType(request.companyType());
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
                .companyRegistrationDate(company.getCompanyRegistrationDate())
                .companyType(company.getCompanyType())
                .userId(company.getUserId())
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


}
