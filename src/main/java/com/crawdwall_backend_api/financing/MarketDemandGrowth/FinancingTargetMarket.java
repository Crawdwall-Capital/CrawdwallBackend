package com.crawdwall_backend_api.financing.marketdemandgrowth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingTargetMarket {
    private String primaryTargetMarket;
    private String otherTargetMarket;
    private Integer estimatedAudienceSize;
    private String distributionStrategy;
}
