package com.crawdwall_backend_api.financing.request;

import java.time.LocalDate;

public record ExecutionDetailsRequest(
    String expectedLocation,
    LocalDate projectStartDate,
    LocalDate projectCompletionDate,
    String projectInitiativeType,
    String recurringHistory,
    String pastProjectLinks
) {}
