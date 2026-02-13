package com.crawdwall_backend_api.company;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


import com.crawdwall_backend_api.utils.ApiResponse;
import com.crawdwall_backend_api.company.request.CompanyCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyUpdateRequest;
import com.crawdwall_backend_api.company.response.CompanyResponse;
import com.crawdwall_backend_api.company.response.CompanyKyc1ReviewResponse;
import com.crawdwall_backend_api.utils.PaginatedData;
import org.springframework.web.bind.annotation.*;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;

import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.company.request.CompanyProfileSetUpCreateRequest;
import com.crawdwall_backend_api.company.companyKycOne.request.DocumentEntityValueCreateRequestSetUp;
import com.crawdwall_backend_api.company.companyKycOne.request.CompanyDeclarationConsentCreateRequest;
import com.crawdwall_backend_api.company.response.CompanyAuthResponse;
import com.crawdwall_backend_api.userauthmgt.user.request.UserAuthRequest;
import com.crawdwall_backend_api.company.request.CompanyBankingAndFinancialAccountsRequest;
import com.crawdwall_backend_api.company.request.CompanyAuthorizedSignatoriesAndControlCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyFinancialIntegrityAndRiskControlCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyExecutionAndReportingReadinessCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyCapitalGovernanceAgreementCreateRequest;


@RestController
@RequestMapping("/api/v1/company")
@RequiredArgsConstructor    
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping("/public/create")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody CompanyCreateRequest request) {
        companyService.createCompany(request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company created successfully")
        .build());
    }

    @PutMapping("/private/update/{id}")
    public ResponseEntity<ApiResponse> updateCompany(@PathVariable(name = "id") String id, @RequestBody CompanyUpdateRequest request) {
        companyService.updateCompany(request, id);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company updated successfully")
        .build());
    }
    @GetMapping("/private/get/{id}")
    public ResponseEntity<ApiResponse> getCompanyById(@PathVariable(name = "id") String id) {
        CompanyResponse companyResponse = companyService.getCompanyById(id);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company fetched successfully")
        .data(companyResponse)
        .build());
    }

    @GetMapping("/private/get-by-user-id/{userId}")
    public ResponseEntity<ApiResponse> getCompanyByUserId(@PathVariable(name = "userId") String userId) {
        CompanyResponse companyResponse = companyService.getCompanyByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company fetched successfully")
        .data(companyResponse)
        .build());
    }

    @GetMapping("/admin/private/get-all")
    public ResponseEntity<ApiResponse> getAllCompanies(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "size", defaultValue = "10") int size, @RequestParam(name = "search", defaultValue = "") String search) {
        PaginatedData paginatedData = companyService.getAllCompanies(page, size, search);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Companies fetched successfully")
        .data(paginatedData)
        .build());
    }


    @PutMapping("/admin/private/activate/{id}")
    public ResponseEntity<ApiResponse> activateCompany(@PathVariable(name = "id") String id) {
        companyService.activateCompany(id);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company activated successfully")
        .build());
    }

    @PutMapping("/admin/private/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateCompany(@PathVariable(name = "id") String id) {
        companyService.deactivateCompany(id);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company deactivated successfully")
        .build());
    }

    @PutMapping("/admin/private/block/{id}")
    public ResponseEntity<ApiResponse> blockCompany(@PathVariable(name = "id") String id) {
        companyService.blockCompany(id);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company blocked successfully")
        .build());
    }
    
    @PutMapping("/admin/private/unblock/{id}")
    public ResponseEntity<ApiResponse> unblockCompany(@PathVariable(name = "id") String id) {
        companyService.unblockCompany(id);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company unblocked successfully")
        .build());
    }

    @PutMapping("/private/change-password/{userId}")
    public ResponseEntity<ApiResponse> changeCompanyPassword(@PathVariable(name = "userId") String userId, @RequestBody PasswordChangeRequest request) {
        companyService.changeCompanyPassword(userId, request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company password changed successfully")
        .build());
    }

    @PutMapping("/public/initiate-reset-password/{emailAddress}")
    public ResponseEntity<ApiResponse> initiateResetPassword(@PathVariable(name = "emailAddress") String emailAddress) {
        companyService.initiateResetPassword(emailAddress);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Otp sent successfully")
        .build());
    }
    
    @PutMapping("/public/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody UserVerifyOtpRequest request) {
        companyService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Otp verified successfully")
        .build());
    }
    
    @PutMapping("/public/resend-otp/{emailAddress}")
    public ResponseEntity<ApiResponse> resendOtp(@PathVariable(name = "emailAddress") String emailAddress, @RequestParam(name = "otpType") UserOtpType userOtpType) {
        companyService.resendOtp(emailAddress, userOtpType);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Otp resent successfully")
        .build());
    }
    
    @PostMapping("/private/setup-profile/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyProfile(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyProfileSetUpCreateRequest request) {
        companyService.setUpCompanyProfile(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company profile setup successfully")
        .build());
    }


    @PostMapping("/public/authenticate")
    ResponseEntity<ApiResponse> authenticate(@RequestBody UserAuthRequest request) {
        CompanyAuthResponse companyAuthResponse = companyService.authenticateCompany(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Company authenticated successfully.")
                .data(companyAuthResponse)
                .build());
    }

    @PostMapping("/private/setup-banking/{companyId}")
    public ResponseEntity<ApiResponse> setUpBankingAndFinancialAccounts(
            @PathVariable(name = "companyId") String companyId, 
            @RequestBody CompanyBankingAndFinancialAccountsRequest request) {
        companyService.setUpBankingAndFinancialAccounts(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Banking and financial accounts setup successfully")
                .build());
    }

    @PostMapping("/private/setup-signatories/{companyId}")
    public ResponseEntity<ApiResponse> setUpAuthorizedSignatoriesAndControl(
            @PathVariable(name = "companyId") String companyId, 
            @RequestBody CompanyAuthorizedSignatoriesAndControlCreateRequest request) {
        companyService.setUpAuthorizedSignatoriesAndControl(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Authorized signatories and control setup successfully")
                .build());
    }

    @PostMapping("/private/setup-financial-integrity/{companyId}")
    public ResponseEntity<ApiResponse> setUpFinancialIntegrityAndRiskControl(
            @PathVariable(name = "companyId") String companyId, 
            @RequestBody CompanyFinancialIntegrityAndRiskControlCreateRequest request) {
        companyService.setUpFinancialIntegrityAndRiskControl(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Financial integrity and risk controls setup successfully")
                .build());
    }

    @PostMapping("/private/setup-execution-readiness/{companyId}")
    public ResponseEntity<ApiResponse> setUpExecutionAndReportingReadiness(
            @PathVariable(name = "companyId") String companyId, 
            @RequestBody CompanyExecutionAndReportingReadinessCreateRequest request) {
        companyService.setUpExecutionAndReportingReadiness(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Execution and reporting readiness setup successfully")
                .build());
    }

    @PostMapping("/private/setup-capital-governance/{companyId}")
    public ResponseEntity<ApiResponse> setUpCapitalGovernanceAgreement(
            @PathVariable(name = "companyId") String companyId, 
            @RequestBody CompanyCapitalGovernanceAgreementCreateRequest request) {
        companyService.setUpCapitalGovernanceAgreement(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Capital governance agreement setup successfully")
                .build());
    }

    @GetMapping("/private/company-review/{companyId}")
    public ResponseEntity<ApiResponse> getKyc1Review(@PathVariable(name = "companyId") String companyId) {
        CompanyKyc1ReviewResponse reviewData = companyService.getKyc1ReviewData(companyId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("KYC Level 1 review data retrieved successfully")
                .data(reviewData)
                .build());
    }

    @PostMapping("/private/company-submit/{companyId}")
    public ResponseEntity<ApiResponse> submitKyc1(@PathVariable(name = "companyId") String companyId) {
        companyService.submitKyc1(companyId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("KYC Level 1 submitted successfully. You can now proceed to KYC Level 2.")
                .build());
    }

}
