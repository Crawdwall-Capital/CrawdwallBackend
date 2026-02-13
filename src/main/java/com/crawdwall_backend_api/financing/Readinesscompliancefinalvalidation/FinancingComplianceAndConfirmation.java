package com.crawdwall_backend_api.financing.readinesscompliancefinalvalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingComplianceAndConfirmation {
    private String regulatoryConsiderations;
    private Boolean milestoneDisbursementMonitoring;
    private Boolean executionMonitoringConsent;
    private String additionalContext;
    private Boolean informationAccuracyConfirmation;
}
