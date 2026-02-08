package com.crawdwall_backend_api.company;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDeclarationConsent {
    
    private boolean agreedToCrawdwallTermsAndConditions;
    private boolean confirmedAllSubmittedInformationAreCorrect;
    private boolean authorizedCrawdwallToPerformBackgroundChecks;
    private String declarationConsentSignatureUrl;
    private LocalDateTime declarationConsentSignedAt;
}
