package com.crawdwall_backend_api.financing.executionoperationplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingMilestonesTimeline {
    private List<FinancingMilestone> milestones;
}
