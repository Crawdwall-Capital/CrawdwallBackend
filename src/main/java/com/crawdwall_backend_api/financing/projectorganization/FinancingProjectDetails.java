package com.crawdwall_backend_api.financing.projectorganization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingProjectDetails {
    private String projectName;
    private String briefProjectDescription;
    private String typeOfProject;
    private String otherProjectType;
    private String problemSolved;
    private String targetAudience;
}
