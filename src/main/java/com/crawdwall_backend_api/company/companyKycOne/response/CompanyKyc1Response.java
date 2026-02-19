package com.crawdwall_backend_api.company.companyKycOne.response;

import java.util.Set;
import java.time.LocalDateTime;

import com.crawdwall_backend_api.company.companyKycOne.CompanyLeaderShipOwnerShip;
import com.crawdwall_backend_api.company.companyKycOne.CompanyTrackRecordCredibility;
import com.crawdwall_backend_api.company.DocumentEntityValue;
import com.crawdwall_backend_api.company.CompanyDeclarationConsent;
import com.crawdwall_backend_api.company.companyKycOne.KycApprovalStage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyKyc1Response {
    

    private String companyId;
    private Set<CompanyLeaderShipOwnerShip> companyLeaderShipOwnerShip;
    private CompanyTrackRecordCredibility companyTrackRecordCredibility;
    private   DocumentEntityValue taxIdentificationDocument;
    private   DocumentEntityValue certificateOfIncorporation;
    private   DocumentEntityValue proofOfAddressDocument;
    private   DocumentEntityValue governmentIdDocument;
    private   DocumentEntityValue complianceAndIdentityDocument;
    private CompanyDeclarationConsent companyDeclarationConsent;
    private boolean kycComplete;
    private KycApprovalStage kycApprovalStage;
    private LocalDateTime kycCompletedAt;
    private LocalDateTime kycStartedAt;
}
