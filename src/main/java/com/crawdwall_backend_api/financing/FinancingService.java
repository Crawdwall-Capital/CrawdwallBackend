package com.crawdwall_backend_api.financing;

import com.crawdwall_backend_api.company.Company;
import com.crawdwall_backend_api.company.CompanyRepository;
import com.crawdwall_backend_api.financing.projectorganization.FinancingPrimaryContact;
import com.crawdwall_backend_api.financing.projectorganization.FinancingProjectDetails;
import com.crawdwall_backend_api.financing.projectorganization.FinancingExecutionDetails;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingExecutionPlan;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingMilestone;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingMilestonesTimeline;
import com.crawdwall_backend_api.financing.executionoperationplan.FinancingTeamMember;
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
import com.crawdwall_backend_api.financing.request.FinancingCreateRequest;
import com.crawdwall_backend_api.financing.request.PrimaryContactRequest;
import com.crawdwall_backend_api.financing.request.ProjectDetailsRequest;
import com.crawdwall_backend_api.financing.request.ExecutionDetailsRequest;
import com.crawdwall_backend_api.financing.request.FinancingExecutionPlanRequest;
import com.crawdwall_backend_api.financing.request.FinancingMilestonesTimelineRequest;
import com.crawdwall_backend_api.financing.request.FinancingMilestoneItemRequest;
import com.crawdwall_backend_api.financing.request.FinancingTeamAndRiskRequest;
import com.crawdwall_backend_api.financing.request.FinancingTeamMemberRequest;
import com.crawdwall_backend_api.financing.request.FinancingVendorsInfrastructureRequest;
import com.crawdwall_backend_api.financing.request.FinancingTargetMarketRequest;
import com.crawdwall_backend_api.financing.request.FinancingMarketingDistributionRequest;
import com.crawdwall_backend_api.financing.request.FinancingCompetitiveAdvantageRequest;
import com.crawdwall_backend_api.financing.request.FinancingPerformanceMetricsRequest;
import com.crawdwall_backend_api.financing.request.FinancingFundingRequestOverviewRequest;
import com.crawdwall_backend_api.financing.request.FinancingBudgetBreakdownRequest;
import com.crawdwall_backend_api.financing.request.FinancingRevenueOutlookRequest;
import com.crawdwall_backend_api.financing.request.FinancingFundingHistoryRequest;
import com.crawdwall_backend_api.financing.request.FinancingExecutionReadinessRequest;
import com.crawdwall_backend_api.financing.request.FinancingComplianceAndConfirmationRequest;
import com.crawdwall_backend_api.utils.ApiResponseMessages;
import com.crawdwall_backend_api.utils.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FinancingService {
    
    private final FinancingRepository financingRepository;
    private final CompanyRepository companyRepository;
    
    public Financing createFinancing(FinancingCreateRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new InvalidInputException(ApiResponseMessages.ERROR_COMPANY_NOT_FOUND));
        
        Financing financing = Financing.builder()
                .companyId(request.companyId())
                .status(FinancingStatus.DRAFT)
                .completedSteps(new HashSet<>())
                .completedProjectOrgSteps(new HashSet<>())
                .completedExecutionOperationSteps(new HashSet<>())
                .completedMarketDemandGrowthSteps(new HashSet<>())
                .completedFinancialFundingRequestSteps(new HashSet<>())
                .completedReadinessComplianceSteps(new HashSet<>())
                .build();
        
        return financingRepository.save(financing);
    }
    
    public void savePrimaryContact(String financingId, PrimaryContactRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
                FinancingPrimaryContact primaryContact = FinancingPrimaryContact.builder()
                .directorName(request.directorName())
                .role(request.role())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .nationality(request.nationality())
                .ownershipPercentage(request.ownershipPercentage())
                .build();
        
        financing.setPrimaryContact(primaryContact);
        
        financing.getCompletedProjectOrgSteps().add(FinancingProjectOrganizationStep.PRIMARY_CONTACT);
        
        if (financing.getCompletedProjectOrgSteps().size() == 3) {
            financing.getCompletedSteps().add(FinancingStep.PROJECT_ORGANIZATION_OVERVIEW);
        }
        
        financingRepository.save(financing);
    }
    
    public Financing getFinancingById(String financingId) {
        return financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
    }
    
    public List<Financing> getFinancingsByCompanyId(String companyId) {
        return financingRepository.findByCompanyId(companyId);
    }
    
    public void saveProjectDetails(String financingId, ProjectDetailsRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingProjectDetails projectDetails = FinancingProjectDetails.builder()
                .projectName(request.projectName())
                .briefProjectDescription(request.briefProjectDescription())
                .typeOfProject(request.typeOfProject())
                .otherProjectType(request.otherProjectType())
                .problemSolved(request.problemSolved())
                .targetAudience(request.targetAudience())
                .build();
        
        financing.setProjectDetails(projectDetails);
        financing.setProjectName(request.projectName());
        
        financing.getCompletedProjectOrgSteps().add(FinancingProjectOrganizationStep.PROJECT_DETAILS);
        
        if (financing.getCompletedProjectOrgSteps().size() == 3) {
            financing.getCompletedSteps().add(FinancingStep.PROJECT_ORGANIZATION_OVERVIEW);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveExecutionDetails(String financingId, ExecutionDetailsRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingExecutionDetails executionDetails = FinancingExecutionDetails.builder()
                .expectedLocation(request.expectedLocation())
                .projectStartDate(request.projectStartDate())
                .projectCompletionDate(request.projectCompletionDate())
                .projectInitiativeType(request.projectInitiativeType())
                .recurringHistory(request.recurringHistory())
                .pastProjectLinks(request.pastProjectLinks())
                .build();
        
        financing.setExecutionDetails(executionDetails);
        
        financing.getCompletedProjectOrgSteps().add(FinancingProjectOrganizationStep.EXECUTION_DETAILS);
        
        if (financing.getCompletedProjectOrgSteps().size() == 3) {
            financing.getCompletedSteps().add(FinancingStep.PROJECT_ORGANIZATION_OVERVIEW);
        }
        
        financingRepository.save(financing);
    }
    
    
    public void saveExecutionPlan(String financingId, FinancingExecutionPlanRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingExecutionPlan executionPlan = FinancingExecutionPlan.builder()
                .executionDescription(request.executionDescription())
                .supportingDocuments(request.supportingDocuments())
                .build();
        
        financing.setExecutionPlan(executionPlan);
        financing.getCompletedExecutionOperationSteps().add(FinancingExecutionOperationStep.EXECUTION_PLAN);
        
        if (financing.getCompletedExecutionOperationSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.EXECUTION_OPERATION_PLAN);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveMilestonesTimeline(String financingId, FinancingMilestonesTimelineRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        List<FinancingMilestone> milestones = request.milestones().stream()
                .map(item -> FinancingMilestone.builder()
                        .phaseName(item.phaseName())
                        .proposedStartDate(item.proposedStartDate())
                        .proposedEndDate(item.proposedEndDate())
                        .build())
                .collect(Collectors.toList());
        
        FinancingMilestonesTimeline milestonesTimeline = FinancingMilestonesTimeline.builder()
                .milestones(milestones)
                .build();
        
        financing.setMilestonesTimeline(milestonesTimeline);
        financing.getCompletedExecutionOperationSteps().add(FinancingExecutionOperationStep.MILESTONES_TIMELINE);
        
        if (financing.getCompletedExecutionOperationSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.EXECUTION_OPERATION_PLAN);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveTeamAndRisk(String financingId, FinancingTeamAndRiskRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        List<FinancingTeamMember> teamMembers = request.teamMembers().stream()
                .map(member -> FinancingTeamMember.builder()
                        .fullName(member.fullName())
                        .roleTitle(member.roleTitle())
                        .relevantExperience(member.relevantExperience())
                        .build())
                .collect(Collectors.toList());
        
        FinancingTeamAndRisk teamAndRisk = FinancingTeamAndRisk.builder()
                .teamMembers(teamMembers)
                .build();
        
        financing.setTeamAndRisk(teamAndRisk);
        financing.getCompletedExecutionOperationSteps().add(FinancingExecutionOperationStep.TEAM_AND_RISK);
        
        if (financing.getCompletedExecutionOperationSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.EXECUTION_OPERATION_PLAN);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveVendorsInfrastructure(String financingId, FinancingVendorsInfrastructureRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingVendorsInfrastructure vendorsInfrastructure = FinancingVendorsInfrastructure.builder()
                .keyOperationalRisks(request.keyOperationalRisks())
                .dependenciesBeforeExecution(request.dependenciesBeforeExecution())
                .productionVendorConfirmed(request.productionVendorConfirmed())
                .venueInfrastructureVendorConfirmed(request.venueInfrastructureVendorConfirmed())
                .logisticsVendorConfirmed(request.logisticsVendorConfirmed())
                .technologyVendorConfirmed(request.technologyVendorConfirmed())
                .marketingVendorConfirmed(request.marketingVendorConfirmed())
                .vendorConstraints(request.vendorConstraints())
                .build();
        
        financing.setVendorsInfrastructure(vendorsInfrastructure);
        financing.getCompletedExecutionOperationSteps().add(FinancingExecutionOperationStep.VENDORS_INFRASTRUCTURE);
        
        if (financing.getCompletedExecutionOperationSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.EXECUTION_OPERATION_PLAN);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveTargetMarket(String financingId, FinancingTargetMarketRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingTargetMarket targetMarket = FinancingTargetMarket.builder()
                .primaryTargetMarket(request.primaryTargetMarket())
                .otherTargetMarket(request.otherTargetMarket())
                .estimatedAudienceSize(request.estimatedAudienceSize())
                .distributionStrategy(request.distributionStrategy())
                .build();
        
        financing.setTargetMarket(targetMarket);
        financing.getCompletedMarketDemandGrowthSteps().add(FinancingMarketDemandGrowthStep.TARGET_MARKET_REACH);
        
        if (financing.getCompletedMarketDemandGrowthSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.MARKET_DEMAND_GROWTH);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveMarketingDistribution(String financingId, FinancingMarketingDistributionRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingMarketingDistribution marketingDistribution = FinancingMarketingDistribution.builder()
                .marketingChannels(request.marketingChannels())
                .strategicPartners(request.strategicPartners())
                .build();
        
        financing.setMarketingDistribution(marketingDistribution);
        financing.getCompletedMarketDemandGrowthSteps().add(FinancingMarketDemandGrowthStep.MARKETING_DISTRIBUTION);
        
        if (financing.getCompletedMarketDemandGrowthSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.MARKET_DEMAND_GROWTH);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveCompetitiveAdvantage(String financingId, FinancingCompetitiveAdvantageRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingCompetitiveAdvantage competitiveAdvantage = FinancingCompetitiveAdvantage.builder()
                .differentiation(request.differentiation())
                .scalabilityPotential(request.scalabilityPotential())
                .build();
        
        financing.setCompetitiveAdvantage(competitiveAdvantage);
        financing.getCompletedMarketDemandGrowthSteps().add(FinancingMarketDemandGrowthStep.COMPETITIVE_ADVANTAGE_AND_GROWTH);
        
        if (financing.getCompletedMarketDemandGrowthSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.MARKET_DEMAND_GROWTH);
        }
        
        financingRepository.save(financing);
    }
    
    public void savePerformanceMetrics(String financingId, FinancingPerformanceMetricsRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingPerformanceMetrics performanceMetrics = FinancingPerformanceMetrics.builder()
                .attendance(request.attendance())
                .revenue(request.revenue())
                .revenueCurrency(request.revenueCurrency())
                .growthRate(request.growthRate())
                .build();
        
        financing.setPerformanceMetrics(performanceMetrics);
        financing.getCompletedMarketDemandGrowthSteps().add(FinancingMarketDemandGrowthStep.PERFORMANCE_METRICS);
        
        if (financing.getCompletedMarketDemandGrowthSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.MARKET_DEMAND_GROWTH);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveFundingRequestOverview(String financingId, FinancingFundingRequestOverviewRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingFundingRequestOverview fundingRequestOverview = FinancingFundingRequestOverview.builder()
                .totalCapitalRequired(request.totalCapitalRequired())
                .capitalCurrency(request.capitalCurrency())
                .intendedUseOfFunds(request.intendedUseOfFunds())
                .build();
        
        financing.setFundingRequestOverview(fundingRequestOverview);
        financing.getCompletedFinancialFundingRequestSteps().add(FinancingFinancialFundingRequestStep.FUNDING_REQUEST_OVERVIEW);
        
        if (financing.getCompletedFinancialFundingRequestSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.FINANCIAL_FUNDING_REQUEST);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveBudgetBreakdown(String financingId, FinancingBudgetBreakdownRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingBudgetBreakdown budgetBreakdown = FinancingBudgetBreakdown.builder()
                .production(request.production())
                .venue(request.venue())
                .marketing(request.marketing())
                .staffing(request.staffing())
                .logistics(request.logistics())
                .technology(request.technology())
                .contingency(request.contingency())
                .otherCategory(request.otherCategory())
                .otherAmount(request.otherAmount())
                .total(request.total())
                .build();
        
        financing.setBudgetBreakdown(budgetBreakdown);
        financing.getCompletedFinancialFundingRequestSteps().add(FinancingFinancialFundingRequestStep.BUDGET_BREAKDOWN);
        
        if (financing.getCompletedFinancialFundingRequestSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.FINANCIAL_FUNDING_REQUEST);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveRevenueOutlook(String financingId, FinancingRevenueOutlookRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingRevenueOutlook revenueOutlook = FinancingRevenueOutlook.builder()
                .expectedRevenueSources(request.expectedRevenueSources())
                .projectedGrossRevenue(request.projectedGrossRevenue())
                .revenueCurrency(request.revenueCurrency())
                .expectedNetSurplus(request.expectedNetSurplus())
                .build();
        
        financing.setRevenueOutlook(revenueOutlook);
        financing.getCompletedFinancialFundingRequestSteps().add(FinancingFinancialFundingRequestStep.REVENUE_OUTLOOK);
        
        if (financing.getCompletedFinancialFundingRequestSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.FINANCIAL_FUNDING_REQUEST);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveFundingHistory(String financingId, FinancingFundingHistoryRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingFundingHistory fundingHistory = FinancingFundingHistory.builder()
                .receivedPriorFunding(request.receivedPriorFunding())
                .fundingSource(request.fundingSource())
                .amountReceived(request.amountReceived())
                .fundingCurrency(request.fundingCurrency())
                .fundingYear(request.fundingYear())
                .financialRisksMitigation(request.financialRisksMitigation())
                .build();
        
        financing.setFundingHistory(fundingHistory);
        financing.getCompletedFinancialFundingRequestSteps().add(FinancingFinancialFundingRequestStep.FUNDING_HISTORY);
        
        if (financing.getCompletedFinancialFundingRequestSteps().size() == 4) {
            financing.getCompletedSteps().add(FinancingStep.FINANCIAL_FUNDING_REQUEST);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveExecutionReadiness(String financingId, FinancingExecutionReadinessRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingExecutionReadiness executionReadiness = FinancingExecutionReadiness.builder()
                .projectStage(request.projectStage())
                .dependenciesBeforeDeployment(request.dependenciesBeforeDeployment())
                .build();
        
        financing.setExecutionReadiness(executionReadiness);
        financing.getCompletedReadinessComplianceSteps().add(FinancingReadinessComplianceStep.EXECUTION_READINESS);
        
        if (financing.getCompletedReadinessComplianceSteps().size() == 2) {
            financing.getCompletedSteps().add(FinancingStep.READINESS_COMPLIANCE);
        }
        
        financingRepository.save(financing);
    }
    
    public void saveComplianceAndConfirmation(String financingId, FinancingComplianceAndConfirmationRequest request) {
        Financing financing = financingRepository.findById(financingId)
                .orElseThrow(() -> new InvalidInputException("Financing request not found"));
        
        FinancingComplianceAndConfirmation complianceAndConfirmation = FinancingComplianceAndConfirmation.builder()
                .regulatoryConsiderations(request.regulatoryConsiderations())
                .milestoneDisbursementMonitoring(request.milestoneDisbursementMonitoring())
                .executionMonitoringConsent(request.executionMonitoringConsent())
                .additionalContext(request.additionalContext())
                .informationAccuracyConfirmation(request.informationAccuracyConfirmation())
                .build();
        
        financing.setComplianceAndConfirmation(complianceAndConfirmation);
        financing.getCompletedReadinessComplianceSteps().add(FinancingReadinessComplianceStep.COMPLIANCE_AND_CONFIRMATION);
        
        if (financing.getCompletedReadinessComplianceSteps().size() == 2) {
            financing.getCompletedSteps().add(FinancingStep.READINESS_COMPLIANCE);
        }
        
        financingRepository.save(financing);
    }
}
