package com.crawdwall_backend_api.financing.executionoperationplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingTeamMember {
    private String fullName;
    private String roleTitle;
    private String relevantExperience;
}
