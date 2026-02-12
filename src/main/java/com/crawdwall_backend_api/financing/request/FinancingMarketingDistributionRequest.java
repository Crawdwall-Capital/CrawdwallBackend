package com.crawdwall_backend_api.financing.request;

import java.util.List;

public record FinancingMarketingDistributionRequest(
    List<String> marketingChannels,
    String strategicPartners
) {}
