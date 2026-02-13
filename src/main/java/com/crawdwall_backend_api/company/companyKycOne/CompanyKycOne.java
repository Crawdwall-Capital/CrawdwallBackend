package com.crawdwall_backend_api.company.companyKycOne;

import com.crawdwall_backend_api.company.CompanyDeclarationConsent;
import com.crawdwall_backend_api.company.DocumentEntityValue;
import com.crawdwall_backend_api.utils.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;
import java.time.LocalDateTime;

import com.crawdwall_backend_api.company.companyKycOne.KycApprovalStage;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "company_kyc_one")
@EqualsAndHashCode(callSuper = true)
public class CompanyKycOne extends BaseEntity{

    @Indexed(unique = true)
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
