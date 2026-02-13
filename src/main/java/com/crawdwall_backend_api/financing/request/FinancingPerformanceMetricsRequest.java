package com.crawdwall_backend_api.financing.request;

public record FinancingPerformanceMetricsRequest(
    Integer attendance,
    Double revenue,
    String revenueCurrency,
    String growthRate
) {}
