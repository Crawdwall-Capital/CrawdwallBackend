package com.crawdwall_backend_api.financing.request;

public record FinancingComplianceAndConfirmationRequest(
    String regulatoryConsiderations,
    Boolean milestoneDisbursementMonitoring,
    Boolean executionMonitoringConsent,
    String additionalContext,
    Boolean informationAccuracyConfirmation
) {}
