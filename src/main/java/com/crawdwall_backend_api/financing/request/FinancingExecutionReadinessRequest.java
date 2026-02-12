package com.crawdwall_backend_api.financing.request;

public record FinancingExecutionReadinessRequest(
    String projectStage,
    String dependenciesBeforeDeployment
) {}
