package com.crawdwall_backend_api.financing.request;

import java.util.List;

public record FinancingTeamAndRiskRequest(
    List<FinancingTeamMemberRequest> teamMembers
) {}
