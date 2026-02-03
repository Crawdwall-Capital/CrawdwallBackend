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

    @PutMapping("/private/initiate-reset-password/{emailAddress}")
    public ResponseEntity<ApiResponse> initiateResetPassword(@PathVariable(name = "emailAddress") String emailAddress) {
        companyService.initiateResetPassword(emailAddress);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Otp sent successfully")
        .build());
    }
    
    @PutMapping("/private/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody UserVerifyOtpRequest request) {
        companyService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Otp verified successfully")
        .build());
    }
    
    @PutMapping("/private/resend-otp/{emailAddress}")
    public ResponseEntity<ApiResponse> resendOtp(@PathVariable(name = "emailAddress") String emailAddress, @RequestParam(name = "otpType") UserOtpType userOtpType) {
        companyService.resendOtp(emailAddress, userOtpType);
        return ResponseEntity.ok(ApiResponse.builder()
        .success(true).message("Otp resent successfully")
        .build());
    }
    
    
}
