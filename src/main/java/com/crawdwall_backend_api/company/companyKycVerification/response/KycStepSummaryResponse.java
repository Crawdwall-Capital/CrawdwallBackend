package com.crawdwall_backend_api.company.companyKycVerification.response;

import com.crawdwall_backend_api.company.companyKycVerification.KycStepStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KycStepSummaryResponse {
    
    private String stepName;
    private int stepNumber;
    private KycStepStatus status;
    private boolean canProceedToNext;
    private boolean isCompleted;
    private String nextStepUrl;
    private int progressPercentage;
}