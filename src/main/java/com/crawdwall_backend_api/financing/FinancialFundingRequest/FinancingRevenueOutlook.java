package com.crawdwall_backend_api.financing.financialfundingrequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingRevenueOutlook {
    private String expectedRevenueSources;
    private Double projectedGrossRevenue;
    private String revenueCurrency;
    private Double expectedNetSurplus;
}
