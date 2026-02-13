package com.crawdwall_backend_api.financing.request;

public record FinancingRevenueOutlookRequest(
    String expectedRevenueSources,
    Double projectedGrossRevenue,
    String revenueCurrency,
    Double expectedNetSurplus
) {}
