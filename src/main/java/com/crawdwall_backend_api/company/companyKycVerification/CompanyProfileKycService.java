package com.crawdwall_backend_api.company.companyKycVerification;

import com.crawdwall_backend_api.company.CompanyRepository;
import com.crawdwall_backend_api.company.companyKycVerification.request.CompanyProfileKycRequest;
import com.crawdwall_backend_api.company.companyKycVerification.response.CompanyProfileKycResponse;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.ValidationUtils;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import com.crawdwall_backend_api.utils.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyProfileKycService {

    private final CompanyProfileKycRepository companyProfileKycRepository;
    private final CompanyRepository companyRepository;

    public CompanyProfileKycResponse saveCompanyProfile(CompanyProfileKycRequest request, String userEmail) {
        // Find company by user email
        String companyId = getCompanyIdByUserEmail(userEmail);
        
        // Basic validation
        validateBasicFields(request);

        // Check if already submitted
        Optional<CompanyProfileKyc> existing = companyProfileKycRepository.findByCompanyId(companyId);
        if (existing.isPresent() && existing.get().getStepStatus() == KycStepStatus.SUBMITTED) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_COMPANY_PROFILE_ALREADY_SUBMITTED);
        }

        CompanyProfileKyc companyProfileKyc;
        if (existing.isPresent()) {
            // Update existing
            companyProfileKyc = existing.get();
            updateFields(companyProfileKyc, request);
        } else {
            // Create new
            companyProfileKyc = buildNewProfile(request, companyId);
        }

        companyProfileKyc.setStepStatus(KycStepStatus.DRAFT);
        companyProfileKyc.setCanProceedToNext(false);
        companyProfileKyc.setValidated(false);

        CompanyProfileKyc saved = companyProfileKycRepository.save(companyProfileKyc);
        return buildResponse(saved);
    }

    public CompanyProfileKycResponse saveAndContinueCompanyProfile(CompanyProfileKycRequest request, String userEmail) {
        // Find company by user email
        String companyId = getCompanyIdByUserEmail(userEmail);
        
        // Complete validation (stricter)
        validateAllFields(request);

        // Check if already submitted
        Optional<CompanyProfileKyc> existing = companyProfileKycRepository.findByCompanyId(companyId);
        if (existing.isPresent() && existing.get().getStepStatus() == KycStepStatus.SUBMITTED) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_COMPANY_PROFILE_ALREADY_SUBMITTED);
        }

        CompanyProfileKyc companyProfileKyc;
        if (existing.isPresent()) {
            // Update existing
            companyProfileKyc = existing.get();
            updateFields(companyProfileKyc, request);
        } else {
            // Create new
            companyProfileKyc = buildNewProfile(request, companyId);
        }

        companyProfileKyc.setStepStatus(KycStepStatus.COMPLETED);
        companyProfileKyc.setCanProceedToNext(true);
        companyProfileKyc.setValidated(true);

        CompanyProfileKyc saved = companyProfileKycRepository.save(companyProfileKyc);
        return buildResponse(saved);
    }

    public CompanyProfileKycResponse getCompanyProfile(String userEmail) {
        // Find company by user email
        String companyId = getCompanyIdByUserEmail(userEmail);

        CompanyProfileKyc profile = companyProfileKycRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_KYC_COMPANY_PROFILE_NOT_FOUND));

        return buildResponse(profile);
    }

    public boolean hasCompanyProfile(String userEmail) {
        // Find company by user email
        String companyId = getCompanyIdByUserEmail(userEmail);
        return companyProfileKycRepository.existsByCompanyId(companyId);
    }

    public void deleteCompanyProfile(String userEmail) {
        // Find company by user email
        String companyId = getCompanyIdByUserEmail(userEmail);
        
        if (!companyProfileKycRepository.existsByCompanyId(companyId)) {
            throw new ResourceNotFoundException(ApiResponseMessages.ERROR_KYC_COMPANY_PROFILE_NOT_FOUND);
        }
        companyProfileKycRepository.deleteByCompanyId(companyId);
    }

    // Private helper methods
    private String getCompanyIdByUserEmail(String userEmail) {
        Company company = companyRepository.findByCompanyEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        return company.getId();
    }

    private void validateBasicFields(CompanyProfileKycRequest request) {
        if (!ValidationUtils.isValidRequiredString(request.companyName())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_ORGANIZATION_NAME_REQUIRED);
        }

        if (request.companyType() == null) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_ORGANIZATION_TYPE_REQUIRED);
        }

        // Validate formats if provided
        if (StringUtils.hasText(request.companyEmail()) && !ValidationUtils.isValidEmail(request.companyEmail())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_INVALID_EMAIL_FORMAT);
        }

        if (StringUtils.hasText(request.companyPhone()) && !ValidationUtils.isValidPhone(request.companyPhone())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_INVALID_PHONE_FORMAT);
        }

        if (StringUtils.hasText(request.companyWebsite()) && !ValidationUtils.isValidWebsite(request.companyWebsite())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_INVALID_WEBSITE_FORMAT);
        }

        if (!ValidationUtils.isValidPastDate(request.dateEstablished())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_INVALID_DATE_ESTABLISHED);
        }
    }

    private void validateAllFields(CompanyProfileKycRequest request) {
        // All basic validations
        validateBasicFields(request);

        // Required fields for completion
        if (!ValidationUtils.isValidRequiredString(request.countryOfRegistration())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_COUNTRY_REGISTRATION_REQUIRED);
        }

        if (request.dateEstablished() == null) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_DATE_ESTABLISHED_REQUIRED);
        }

        if (request.companyAddress() == null || request.companyAddress().trim().isEmpty()) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_BUSINESS_ADDRESS_REQUIRED);
        }

        if (!ValidationUtils.isValidRequiredString(request.companyEmail())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_COMPANY_EMAIL_REQUIRED);
        }

        if (!ValidationUtils.isValidRequiredString(request.companyPhone())) {
            throw new InvalidInputException(ApiResponseMessages.ERROR_KYC_PHONE_NUMBER_REQUIRED);
        }
    }

    private CompanyProfileKyc buildNewProfile(CompanyProfileKycRequest request, String companyId) {
        return CompanyProfileKyc.builder()
                .companyId(companyId)
                .companyName(request.companyName())
                .companyType(request.companyType())
                .countryOfRegistration(request.countryOfRegistration())
                .dateEstablished(request.dateEstablished())
                .companyAddress(request.companyAddress())
                .companyWebsite(request.companyWebsite())
                .socialMediaLinks(request.socialMediaLinks())
                .companyEmail(request.companyEmail())
                .companyPhone(request.companyPhone())
                .canProceedToNext(false)
                .isValidated(false)
                .build();
    }

    private void updateFields(CompanyProfileKyc existing, CompanyProfileKycRequest request) {
        existing.setCompanyName(request.companyName());
        existing.setCompanyType(request.companyType());
        existing.setCountryOfRegistration(request.countryOfRegistration());
        existing.setDateEstablished(request.dateEstablished());
        existing.setCompanyAddress(request.companyAddress());
        existing.setCompanyWebsite(request.companyWebsite());
        existing.setSocialMediaLinks(request.socialMediaLinks());
        existing.setCompanyEmail(request.companyEmail());
        existing.setCompanyPhone(request.companyPhone());
        existing.setUpdatedAt(LocalDateTime.now());
    }

    private CompanyProfileKycResponse buildResponse(CompanyProfileKyc profile) {
        return CompanyProfileKycResponse.builder()
                .id(profile.getId())
                .companyId(profile.getCompanyId())
                .companyName(profile.getCompanyName())
                .companyType(profile.getCompanyType())
                .countryOfRegistration(profile.getCountryOfRegistration())
                .dateEstablished(profile.getDateEstablished())
                .companyAddress(profile.getCompanyAddress())
                .companyWebsite(profile.getCompanyWebsite())
                .socialMediaLinks(profile.getSocialMediaLinks())
                .companyEmail(profile.getCompanyEmail())
                .companyPhone(profile.getCompanyPhone())
                .stepStatus(profile.getStepStatus())
                .canProceedToNext(profile.isCanProceedToNext())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .isValidated(profile.isValidated())
                .validationNotes(profile.getValidationNotes())
                .build();
    }
}