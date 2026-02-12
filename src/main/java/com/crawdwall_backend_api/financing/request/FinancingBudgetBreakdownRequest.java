package com.crawdwall_backend_api.financing.request;

public record FinancingBudgetBreakdownRequest(
    Double production,
    Double venue,
    Double marketing,
    Double staffing,
    Double logistics,
    Double technology,
    Double contingency,
    String otherCategory,
    Double otherAmount,
    Double total
) {}
