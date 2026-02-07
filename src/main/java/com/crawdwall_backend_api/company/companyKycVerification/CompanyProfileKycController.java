package com.crawdwall_backend_api.company.companyKycVerification;

import com.crawdwall_backend_api.company.companyKycVerification.request.CompanyProfileKycRequest;
import com.crawdwall_backend_api.company.companyKycVerification.response.CompanyProfileKycResponse;
import com.crawdwall_backend_api.utils.ApiResponse;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/kyc/company-profile")
@RequiredArgsConstructor
public class CompanyProfileKycController {

    private final CompanyProfileKycService companyProfileKycService;

    /**
     * Save company profile KYC information as draft
     * User can save incomplete information and continue later
     */
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveCompanyProfile(@RequestBody CompanyProfileKycRequest request) {
        String userEmail = getCurrentUserEmail();
        CompanyProfileKycResponse response = companyProfileKycService.saveCompanyProfile(request, userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(ApiResponseMessages.SUCCESS_KYC_COMPANY_PROFILE_SAVED)
                .data(response)
                .build());
    }

    /**
     * Save company profile KYC information and mark as completed
     * Enables user to proceed to the next KYC step
     */
    @PostMapping("/save-and-continue")
    public ResponseEntity<ApiResponse> saveAndContinueCompanyProfile(@RequestBody CompanyProfileKycRequest request) {
        String userEmail = getCurrentUserEmail();
        CompanyProfileKycResponse response = companyProfileKycService.saveAndContinueCompanyProfile(request, userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(ApiResponseMessages.SUCCESS_KYC_COMPANY_PROFILE_SAVED_AND_CONTINUE)
                .data(response)
                .build());
    }

    /**
     * Get company profile KYC information
     */
    @GetMapping("/get")
    public ResponseEntity<ApiResponse> getCompanyProfile() {
        String userEmail = getCurrentUserEmail();
        CompanyProfileKycResponse response = companyProfileKycService.getCompanyProfile(userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(ApiResponseMessages.SUCCESS_KYC_COMPANY_PROFILE_RETRIEVED)
                .data(response)
                .build());
    }

    /**
     * Check if company profile KYC exists
     * Useful for frontend to determine if user has started this step
     */
    @GetMapping("/exists")
    public ResponseEntity<ApiResponse> checkCompanyProfileExists() {
        String userEmail = getCurrentUserEmail();
        boolean exists = companyProfileKycService.hasCompanyProfile(userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(exists ? "Company profile KYC exists" : "Company profile KYC does not exist")
                .data(exists)
                .build());
    }

    /**
     * Update existing company profile KYC information
     * Same as save but more explicit for updates
     */
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateCompanyProfile(@RequestBody CompanyProfileKycRequest request) {
        String userEmail = getCurrentUserEmail();
        CompanyProfileKycResponse response = companyProfileKycService.saveCompanyProfile(request, userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Company profile updated successfully")
                .data(response)
                .build());
    }

    /**
     * Update and complete company profile KYC information
     * Same as save-and-continue but more explicit for updates
     */
    @PutMapping("/update-and-continue")
    public ResponseEntity<ApiResponse> updateAndContinueCompanyProfile(@RequestBody CompanyProfileKycRequest request) {
        String userEmail = getCurrentUserEmail();
        CompanyProfileKycResponse response = companyProfileKycService.saveAndContinueCompanyProfile(request, userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Company profile updated successfully. You can now proceed to the next step.")
                .data(response)
                .build());
    }

    /**
     * Delete company profile KYC information
     * Admin endpoint or for testing purposes
     */
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse> deleteCompanyProfile() {
        String userEmail = getCurrentUserEmail();
        companyProfileKycService.deleteCompanyProfile(userEmail);
        
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Company profile KYC deleted successfully")
                .build());
    }

    private String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName(); // This returns the email (subject) from JWT token
    }
}