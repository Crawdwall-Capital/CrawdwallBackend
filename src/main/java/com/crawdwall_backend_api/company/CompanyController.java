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
import com.crawdwall_backend_api.utils.PaginatedData;
import org.springframework.web.bind.annotation.*;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;

import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.company.request.CompanyProfileSetUpCreateRequest;
import com.crawdwall_backend_api.company.request.CompanyLeaderShipOwnerShipCreateRequest;
import com.crawdwall_backend_api.company.request.DocumentEntityValueCreateRequestSetUp;
import com.crawdwall_backend_api.company.request.CompanyDeclarationConsentCreateRequest;
import com.crawdwall_backend_api.company.response.CompanyAuthResponse;
import com.crawdwall_backend_api.userauthmgt.user.request.UserAuthRequest;
import com.crawdwall_backend_api.company.response.CompanyAuthResponse;


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


    

    @PostMapping("/private/setup-ownership/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyLeaderShipOwnerShip(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyLeaderShipOwnerShipCreateRequest request) {
        companyService.setUpCompanyLeaderShipOwnerShip(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company leader and ownership setup successfully")
        .build());
    }

    @PostMapping("/private/setup-credibility/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyTrackRecordCredibility(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyTrackRecordCredibilityCreateRequest request) {
        companyService.setUpCompanyTrackRecordCredibility(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company track record credibility setup successfully")
        .build());
    }

    @PostMapping("/private/setup-compliance/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyComplianceAndIdentity(@PathVariable(name = "companyId") String companyId, @RequestBody DocumentEntityValueCreateRequestSetUp request) {
        companyService.setUpCompanyComplianceAndIdentity(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company compliance and identity setup successfully")
        .build());
    }

    @PostMapping("/private/setup-declaration-consent/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyDeclarationConsent(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyDeclarationConsentCreateRequest request) {
        companyService.setUpCompanyDeclarationConsent(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Company declaration consent setup successfully")
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



}
