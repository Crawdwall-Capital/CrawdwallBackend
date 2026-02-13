package com.crawdwall_backend_api.financing.request;

public record FinancingFundingRequestOverviewRequest(
    Double totalCapitalRequired,
    String capitalCurrency,
    String intendedUseOfFunds
) {}
