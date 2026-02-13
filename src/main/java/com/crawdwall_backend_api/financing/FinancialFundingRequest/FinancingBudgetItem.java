package com.crawdwall_backend_api.financing.financialfundingrequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingBudgetItem {
    private String category;
    private Double amount;
}
