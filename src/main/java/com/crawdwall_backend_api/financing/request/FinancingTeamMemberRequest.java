package com.crawdwall_backend_api.financing.request;

public record FinancingTeamMemberRequest(
    String fullName,
    String roleTitle,
    String relevantExperience
) {}
