package com.crawdwall_backend_api.company.companyKycVerification;

public enum KycStepStatus {
    NOT_STARTED,    // Step hasn't been started yet
    DRAFT,          // User started but hasn't completed
    COMPLETED,      // User completed and can move to next step
    SUBMITTED       // Final submission (read-only)
}