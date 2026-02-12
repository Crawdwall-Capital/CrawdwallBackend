package com.crawdwall_backend_api.financing.financialfundingrequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingBudgetBreakdown {
    private Double production;
    private Double venue;
    private Double marketing;
    private Double staffing;
    private Double logistics;
    private Double technology;
    private Double contingency;
    private String otherCategory;
    private Double otherAmount;
    private Double total;
}
