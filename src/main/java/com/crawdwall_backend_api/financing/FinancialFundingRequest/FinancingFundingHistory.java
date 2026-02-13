package com.crawdwall_backend_api.financing.financialfundingrequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingFundingHistory {
    private Boolean receivedPriorFunding;
    private String fundingSource;
    private Double amountReceived;
    private String fundingCurrency;
    private Integer fundingYear;
    private String financialRisksMitigation;
}
