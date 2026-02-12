package com.crawdwall_backend_api.financing.marketdemandgrowth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingMarketingDistribution {
    private List<String> marketingChannels;
    private String strategicPartners;
}
