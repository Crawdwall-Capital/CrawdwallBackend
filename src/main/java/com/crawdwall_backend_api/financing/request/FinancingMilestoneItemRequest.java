package com.crawdwall_backend_api.financing.request;

import java.time.LocalDate;

public record FinancingMilestoneItemRequest(
    String phaseName,
    LocalDate proposedStartDate,
    LocalDate proposedEndDate
) {}
