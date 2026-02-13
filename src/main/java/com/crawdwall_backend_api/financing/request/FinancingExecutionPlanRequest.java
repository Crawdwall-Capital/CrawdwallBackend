package com.crawdwall_backend_api.financing.request;

import java.util.List;

public record FinancingExecutionPlanRequest(
    String executionDescription,
    List<String> supportingDocuments
) {}
