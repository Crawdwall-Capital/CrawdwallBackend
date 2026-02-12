package com.crawdwall_backend_api.financing.projectorganization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingExecutionDetails {
    private String expectedLocation;
    private LocalDate projectStartDate;
    private LocalDate projectCompletionDate;
    private String projectInitiativeType;
    private String recurringHistory;
    private String pastProjectLinks;
}
