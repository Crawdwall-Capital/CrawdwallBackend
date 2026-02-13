package com.crawdwall_backend_api.financing.executionoperationplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingMilestone {
    private String phaseName;
    private LocalDate proposedStartDate;
    private LocalDate proposedEndDate;
}
