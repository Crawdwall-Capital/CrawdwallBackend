package com.crawdwall_backend_api.financing.request;

public record FinancingTargetMarketRequest(
    String primaryTargetMarket,
    String otherTargetMarket,
    Integer estimatedAudienceSize,
    String distributionStrategy
) {}
