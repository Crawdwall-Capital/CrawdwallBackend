package com.crawdwall_backend_api.company.response;

import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.company.CompanySocialMediaType;
import com.crawdwall_backend_api.company.companyKycOne.CompanyLeaderShipOwnerShip;
import com.crawdwall_backend_api.company.companyKycOne.CompanyTrackRecordCredibility;
import com.crawdwall_backend_api.company.CompanyDeclarationConsent;
import com.crawdwall_backend_api.company.DocumentEntityValue;
import com.crawdwall_backend_api.utils.Address;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record CompanyKycLevel1DetailsResponse(
    CompanyProfileSection companyProfile,
    Set<CompanyLeaderShipOwnerShip> leadershipOwnership,
    CompanyTrackRecordCredibility trackRecordCredibility,
    ComplianceIdentitySection complianceIdentity,
    CompanyDeclarationConsent declarationsConsent
) {
    
    @Builder
    public record CompanyProfileSection(
        String companyName,
        CompanyType companyType,
        String countryOfRegistration,
        LocalDate dateEstablished,
        String streetAddress,
        String city,
        String country,
        String website,
        String socialMediaLink,
        CompanySocialMediaType socialMediaType,
        String companyEmail,
        String phoneNumber
    ) {}
    
    @Builder
    public record ComplianceIdentitySection(
        DocumentEntityValue taxIdentificationDocument,
        DocumentEntityValue certificateOfIncorporation,
        DocumentEntityValue proofOfAddressDocument,
        DocumentEntityValue governmentIdDocument,
        DocumentEntityValue complianceAndIdentityDocument
    ) {}
}
