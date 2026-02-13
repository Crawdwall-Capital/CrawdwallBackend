package com.crawdwall_backend_api.company.response;

import com.crawdwall_backend_api.company.*;
import com.crawdwall_backend_api.utils.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyKyc1ReviewResponse {
    
    private String companyName;
    private String companyEmail;
    private String companyPhone;
    private String companyWebsite;
    private CompanyType companyType;
    private LocalDate companyEstablishedDate;
    private CompanySocialMediaType companySocialMediaType;
    private String companySocialMediaUrl;
    private Address companyAddress;
    
    private Set<CompanyLeaderShipOwnerShip> leadershipAndOwnership;
    
    private CompanyTrackRecordCredibility trackRecordAndCredibility;
    
    private DocumentEntityValue taxIdentificationDocument;
    private DocumentEntityValue proofOfAddressDocument;
    private DocumentEntityValue governmentIdDocument;
    private DocumentEntityValue complianceAndIdentityDocument;

    private CompanyDeclarationConsent declarationAndConsent;
}
