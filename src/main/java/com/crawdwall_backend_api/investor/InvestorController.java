package com.crawdwall_backend_api.investor;

import com.crawdwall_backend_api.investor.request.InvestorCreateRequest;
import com.crawdwall_backend_api.investor.request.InvestorUpdateRequest;
import com.crawdwall_backend_api.investor.response.InvestorResponse;
import com.crawdwall_backend_api.utils.ApiResponse;
import com.crawdwall_backend_api.utils.PaginatedData;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;
import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/investor")
@RequiredArgsConstructor
public class InvestorController {

    private final InvestorService investorService;

    @PostMapping("/public/create")
    public ResponseEntity<ApiResponse> createInvestor(@RequestBody InvestorCreateRequest request) {
        investorService.createInvestor(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor created successfully")
                .build());
    }

    @PutMapping("/private/update/{id}")
    public ResponseEntity<ApiResponse> updateInvestor(@PathVariable(name = "id") String id, @RequestBody InvestorUpdateRequest request) {
        investorService.updateInvestor(request, id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor updated successfully")
                .build());
    }

    @GetMapping("/private/get/{id}")
    public ResponseEntity<ApiResponse> getInvestorById(@PathVariable(name = "id") String id) {
        InvestorResponse investorResponse = investorService.getInvestorById(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor fetched successfully")
                .data(investorResponse)
                .build());
    }

    @GetMapping("/private/get-by-user-id/{userId}")
    public ResponseEntity<ApiResponse> getInvestorByUserId(@PathVariable(name = "userId") String userId) {
        InvestorResponse investorResponse = investorService.getInvestorByUserId(userId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor fetched successfully")
                .data(investorResponse)
                .build());
    }

    @GetMapping("/admin/private/get-all")
    public ResponseEntity<ApiResponse> getAllInvestors(@RequestParam(name = "page", defaultValue = "1") int page, 
                                                      @RequestParam(name = "size", defaultValue = "10") int size, 
                                                      @RequestParam(name = "search", defaultValue = "") String search) {
        PaginatedData paginatedData = investorService.getAllInvestors(page, size, search);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investors fetched successfully")
                .data(paginatedData)
                .build());
    }

    @PutMapping("/admin/private/activate/{id}")
    public ResponseEntity<ApiResponse> activateInvestor(@PathVariable(name = "id") String id) {
        investorService.activateInvestor(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor activated successfully")
                .build());
    }

    @PutMapping("/admin/private/deactivate/{id}")
    public ResponseEntity<ApiResponse> deactivateInvestor(@PathVariable(name = "id") String id) {
        investorService.deactivateInvestor(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor deactivated successfully")
                .build());
    }

    @PutMapping("/admin/private/block/{id}")
    public ResponseEntity<ApiResponse> blockInvestor(@PathVariable(name = "id") String id) {
        investorService.blockInvestor(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor blocked successfully")
                .build());
    }

    @PutMapping("/admin/private/unblock/{id}")
    public ResponseEntity<ApiResponse> unblockInvestor(@PathVariable(name = "id") String id) {
        investorService.unblockInvestor(id);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor unblocked successfully")
                .build());
    }

    @PutMapping("/private/change-password/{userId}")
    public ResponseEntity<ApiResponse> changeInvestorPassword(@PathVariable(name = "userId") String userId, @RequestBody PasswordChangeRequest request) {
        investorService.changeInvestorPassword(userId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Investor password changed successfully")
                .build());
    }

    @PutMapping("/private/initiate-reset-password/{emailAddress}")
    public ResponseEntity<ApiResponse> initiateResetPassword(@PathVariable(name = "emailAddress") String emailAddress) {
        investorService.initiateResetPassword(emailAddress);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Otp sent successfully")
                .build());
    }

    @PutMapping("/private/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody UserVerifyOtpRequest request) {
        investorService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Otp verified successfully")
                .build());
    }

    @PutMapping("/private/resend-otp/{emailAddress}")
    public ResponseEntity<ApiResponse> resendOtp(@PathVariable(name = "emailAddress") String emailAddress, @RequestParam(name = "otpType") UserOtpType userOtpType) {
        investorService.resendOtp(emailAddress, userOtpType);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Otp resent successfully")
                .build());
    }
}