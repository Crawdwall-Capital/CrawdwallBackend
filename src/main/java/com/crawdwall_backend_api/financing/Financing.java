package com.crawdwall_backend_api.financing;

import com.crawdwall_backend_api.financing.projectorganization.FinancingPrimaryContact;
import com.crawdwall_backend_api.financing.projectorganization.FinancingProjectDetails;
import com.crawdwall_backend_api.financing.projectorganization.FinancingExecutionDetails;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingExecutionPlan;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingMilestonesTimeline;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingTeamAndRisk;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingVendorsInfrastructure;
import com.crawdwall_backend_api.financing.marketdemandgrowth.FinancingTargetMarket;
import com.crawdwall_backend_api.financing.marketdemandgrowth.FinancingMarketingDistribution;
import com.crawdwall_backend_api.financing.marketdemandgrowth.FinancingCompetitiveAdvantage;
import com.crawdwall_backend_api.financing.marketdemandgrowth.FinancingPerformanceMetrics;
import com.crawdwall_backend_api.financing.financialfundingrequest.FinancingFundingRequestOverview;
import com.crawdwall_backend_api.financing.financialfundingrequest.FinancingBudgetBreakdown;
import com.crawdwall_backend_api.financing.financialfundingrequest.FinancingRevenueOutlook;
import com.crawdwall_backend_api.financing.financialfundingrequest.FinancingFundingHistory;
import com.crawdwall_backend_api.financing.readinesscompliancefinalvalidation.FinancingExecutionReadiness;
import com.crawdwall_backend_api.financing.readinesscompliancefinalvalidation.FinancingComplianceAndConfirmation;
import com.crawdwall_backend_api.utils.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "financings")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Financing extends BaseEntity {
    
    private String companyId;
    private String projectName;
    private FinancingStatus status;
    
    @Builder.Default
    private Set<FinancingStep> completedSteps = new HashSet<>();
    
    @Builder.Default
    private Set<FinancingProjectOrganizationStep> completedProjectOrgSteps = new HashSet<>();
    
    @Builder.Default
    private Set<FinancingExecutionOperationStep> completedExecutionOperationSteps = new HashSet<>();
    
    @Builder.Default
    private Set<FinancingMarketDemandGrowthStep> completedMarketDemandGrowthSteps = new HashSet<>();
    
    @Builder.Default
    private Set<FinancingFinancialFundingRequestStep> completedFinancialFundingRequestSteps = new HashSet<>();
    
    @Builder.Default
    private Set<FinancingReadinessComplianceStep> completedReadinessComplianceSteps = new HashSet<>();
    
    private FinancingPrimaryContact primaryContact;
    private FinancingProjectDetails projectDetails;
    private FinancingExecutionDetails executionDetails;
    
    private FinancingExecutionPlan executionPlan;
    private FinancingMilestonesTimeline milestonesTimeline;
    private FinancingTeamAndRisk teamAndRisk;
    private FinancingVendorsInfrastructure vendorsInfrastructure;
    
    private FinancingTargetMarket targetMarket;
    private FinancingMarketingDistribution marketingDistribution;
    private FinancingCompetitiveAdvantage competitiveAdvantage;
    private FinancingPerformanceMetrics performanceMetrics;
    
    private FinancingFundingRequestOverview fundingRequestOverview;
    private FinancingBudgetBreakdown budgetBreakdown;
    private FinancingRevenueOutlook revenueOutlook;
    private FinancingFundingHistory fundingHistory;
    
    private FinancingExecutionReadiness executionReadiness;
    private FinancingComplianceAndConfirmation complianceAndConfirmation;
    
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime rejectedAt;
    private String reviewedBy;
    private String reviewNotes;
}
