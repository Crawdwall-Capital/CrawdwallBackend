package com.crawdwall_backend_api.company.response;

import lombok.Builder;

@Builder
public record CompanyKycOverviewResponse(
    String companyId,
    String companyName,
    String userId,
    String userStatus,
    String level1Status,
    String level2Status
) {
}
