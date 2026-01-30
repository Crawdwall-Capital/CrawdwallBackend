package com.crawdwall_backend_api.admin;


import com.crawdwall_backend_api.admin.request.AdminCreateRequest;
import com.crawdwall_backend_api.admin.request.AdminFilterRequest;
import com.crawdwall_backend_api.admin.response.AdminAuthResponse;
import com.crawdwall_backend_api.admin.response.AdminResponse;
import com.crawdwall_backend_api.userauthmgt.user.request.PasswordChangeRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.UserAuthRequest;
import com.crawdwall_backend_api.userauthmgt.user.request.UserResetPasswordRequest;
import com.crawdwall_backend_api.userauthmgt.user.response.UserVerifyOtpRequest;
import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.crawdwall_backend_api.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/private/invite")
    ResponseEntity<ApiResponse<Object>> createAdmin(@RequestBody AdminCreateRequest request) {
        adminService.inviteAdmin(request);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Admin created successfully").build());
    }

    @GetMapping("/private/get/{adminId}")
    ResponseEntity<ApiResponse<Object>> getAdminById(@PathVariable String adminId) {
        AdminResponse adminResponse = adminService.getAdminById(adminId);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Admin fetched successfully").data(adminResponse).build());
    }
    


    @PutMapping("/public/reset-password")
    ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody UserResetPasswordRequest request) {
        adminService.resetPassword(request);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Password reset successfully").build());
    }

    @PutMapping("/public/initiate-reset-password/{emailAddress}")
    ResponseEntity<ApiResponse<Object>> initiateResetPassword(@PathVariable String emailAddress) {
        adminService.initiateResetPassword(emailAddress);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Otp sent successfully").build());
    }

    @PutMapping("/private/change-password/{userId}")
    ResponseEntity<ApiResponse<Object>> changePassword(@PathVariable String userId, @RequestBody PasswordChangeRequest request) {
        adminService.changeAdminPassword(userId, request);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Password changed successfully").build());
    }

    @PutMapping("/public/verify-otp")
    ResponseEntity<ApiResponse<Object>> verifyOtp(@RequestBody UserVerifyOtpRequest request) {
        adminService.verifyOtp(request);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Otp verified successfully").build());
    }

    @PutMapping("/public/resend-otp/{emailAddress}")
    ResponseEntity<ApiResponse<Object>> resendOtp(@PathVariable String emailAddress,  @RequestParam(name = "otpType") UserOtpType userOtpType) {
        adminService.resendOtp(emailAddress, userOtpType);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Otp resent successfully").build());
    }


    @GetMapping("/private/search")
    ResponseEntity<ApiResponse<Object>> searchAdmins(@RequestParam(name = "searchParams") String searchParams, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).data(adminService.searchAdmins(searchParams, page, size)).message("Admins searched successfully").build());
    }
    
    @PostMapping("/private/filter")
    ResponseEntity<ApiResponse<Object>> filterAdmins(@RequestBody AdminFilterRequest request, @RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).data(adminService.filterAdmins(request, page, size)).message("Admins filtered successfully").build());
    }


    @GetMapping("/private/fetch-all" )
    ResponseEntity<ApiResponse<Object>> fetchAllAdmins(@RequestParam(name = "page") int page, @RequestParam(name = "size") int size) {
        return ResponseEntity.ok(ApiResponse.builder().success(true).data(adminService.fetchAllAdmin(page, size)).message("Admins fetched successfully").build());
    }

    @PostMapping("/public/login")
    ResponseEntity<ApiResponse<Object>> login(@RequestBody UserAuthRequest request) {
        AdminAuthResponse adminAuthResponse = adminService.authenticateAdmin(request);
        return ResponseEntity.ok(ApiResponse.builder().success(true).data(adminAuthResponse).message("Admin logged in successfully").build());
    }
}
