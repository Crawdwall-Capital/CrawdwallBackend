package com.crawdwall_backend_api.financing.request;

public record FinancingFundingHistoryRequest(
    Boolean receivedPriorFunding,
    String fundingSource,
    Double amountReceived,
    String fundingCurrency,
    Integer fundingYear,
    String financialRisksMitigation
) {}
