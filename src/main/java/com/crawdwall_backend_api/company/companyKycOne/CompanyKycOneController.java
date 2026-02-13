package com.crawdwall_backend_api.company.companyKycOne;

import com.crawdwall_backend_api.company.companyKycOne.request.CompanyTrackRecordCredibilityCreateRequest;
import com.crawdwall_backend_api.company.companyKycOne.request.DocumentEntityValueCreateRequestSetUp;
import com.crawdwall_backend_api.company.companyKycOne.request.CompanyDeclarationConsentCreateRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.crawdwall_backend_api.utils.ApiResponse;
import com.crawdwall_backend_api.company.companyKycOne.request.CompanyLeaderShipOwnerShipCreateRequest;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/company/kyc-one")
@RequiredArgsConstructor
public class CompanyKycOneController {

    private final CompanyKycOneService companyKycOneService;

    @PostMapping("/private/leader-ownership/{companyId}")
    public ResponseEntity<ApiResponse> setUpLeaderAndOwnership(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyLeaderShipOwnerShipCreateRequest request) {
        companyKycOneService.setUpCompanyLeaderShipOwnerShip(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder().success(true).message("Leader and ownership setup successfully").build());
    }

    @PostMapping("/private/setup-credibility/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyTrackRecordCredibility(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyTrackRecordCredibilityCreateRequest request) {
        companyKycOneService.setUpCompanyTrackRecordCredibility(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Company track record credibility setup successfully")
                .build());
    }

    @PostMapping("/private/compliance-identity/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyComplianceAndIdentity(@PathVariable(name = "companyId") String companyId, @RequestBody DocumentEntityValueCreateRequestSetUp request) {
        companyKycOneService.setUpCompanyComplianceAndIdentity(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Company compliance and identity setup successfully")
                .build());
    }

    @PostMapping("/private/declaration-consent/{companyId}")
    public ResponseEntity<ApiResponse> setUpCompanyDeclarationConsent(@PathVariable(name = "companyId") String companyId, @RequestBody CompanyDeclarationConsentCreateRequest request) {
        companyKycOneService.setUpCompanyDeclarationConsent(companyId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("Company declaration consent setup successfully")
                .build());
    }

    @PostMapping("/private/complete-kyc/{companyId}")
    public ResponseEntity<ApiResponse> completeKyc1(@PathVariable(name = "companyId") String companyId) {
        companyKycOneService.completeKyc1(companyId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true).message("KYC Level 1 completed successfully")
                .build());
    }
}
