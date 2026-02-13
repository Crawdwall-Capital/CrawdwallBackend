package com.crawdwall_backend_api.company.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDeclarationConsentCreateRequest {
    
    private boolean agreedToCrawdwallTermsAndConditions;
    private boolean confirmedAllSubmittedInformationAreCorrect;
    private boolean authorizedCrawdwallToPerformBackgroundChecks;
    @NotNull
    private String declarationConsentSignatureUrl;
    @NotNull
    private LocalDateTime declarationConsentSignedAt;

  
}
