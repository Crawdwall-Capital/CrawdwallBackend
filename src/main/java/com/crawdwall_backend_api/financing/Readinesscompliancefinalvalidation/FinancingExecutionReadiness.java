package com.crawdwall_backend_api.financing.readinesscompliancefinalvalidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingExecutionReadiness {
    private String projectStage;
    private String dependenciesBeforeDeployment;
}
