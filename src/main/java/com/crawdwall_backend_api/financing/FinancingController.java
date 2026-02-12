package com.crawdwall_backend_api.financing;

import com.crawdwall_backend_api.utils.ApiResponse;
import com.crawdwall_backend_api.financing.request.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/company/private/{companyId}/financing")
@RequiredArgsConstructor
public class FinancingController {
    
    private final FinancingService financingService;
    
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createFinancing(
            @PathVariable String companyId,
            @RequestBody FinancingCreateRequest request) {
        Financing financing = financingService.createFinancing(request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Financing request created successfully")
                .data(financing)
                .build());
    }
    
    @PutMapping("/{financingId}/primary-contact")
    public ResponseEntity<ApiResponse> savePrimaryContact(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody PrimaryContactRequest request) {
        financingService.savePrimaryContact(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Primary contact saved successfully")
                .build());
    }
    
    @GetMapping("/{financingId}")
    public ResponseEntity<ApiResponse> getFinancing(
            @PathVariable String companyId,
            @PathVariable String financingId) {
        Financing financing = financingService.getFinancingById(financingId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Financing request retrieved successfully")
                .data(financing)
                .build());
    }
    
    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getFinancingsByCompany(@PathVariable String companyId) {
        List<Financing> financings = financingService.getFinancingsByCompanyId(companyId);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Financing requests retrieved successfully")
                .data(financings)
                .build());
    }
    
    @PutMapping("/{financingId}/project-details")
    public ResponseEntity<ApiResponse> saveProjectDetails(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody ProjectDetailsRequest request) {
        financingService.saveProjectDetails(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Project details saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/execution-details")
    public ResponseEntity<ApiResponse> saveExecutionDetails(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody ExecutionDetailsRequest request) {
        financingService.saveExecutionDetails(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Execution details saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/execution-plan")
    public ResponseEntity<ApiResponse> saveExecutionPlan(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingExecutionPlanRequest request) {
        financingService.saveExecutionPlan(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Execution plan saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/milestones-timeline")
    public ResponseEntity<ApiResponse> saveMilestonesTimeline(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingMilestonesTimelineRequest request) {
        financingService.saveMilestonesTimeline(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Milestones and timeline saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/team-and-risk")
    public ResponseEntity<ApiResponse> saveTeamAndRisk(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingTeamAndRiskRequest request) {
        financingService.saveTeamAndRisk(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Team and risk saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/vendors-infrastructure")
    public ResponseEntity<ApiResponse> saveVendorsInfrastructure(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingVendorsInfrastructureRequest request) {
        financingService.saveVendorsInfrastructure(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Vendors and infrastructure saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/target-market")
    public ResponseEntity<ApiResponse> saveTargetMarket(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingTargetMarketRequest request) {
        financingService.saveTargetMarket(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Target market saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/marketing-distribution")
    public ResponseEntity<ApiResponse> saveMarketingDistribution(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingMarketingDistributionRequest request) {
        financingService.saveMarketingDistribution(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Marketing and distribution saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/competitive-advantage")
    public ResponseEntity<ApiResponse> saveCompetitiveAdvantage(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingCompetitiveAdvantageRequest request) {
        financingService.saveCompetitiveAdvantage(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Competitive advantage saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/performance-metrics")
    public ResponseEntity<ApiResponse> savePerformanceMetrics(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingPerformanceMetricsRequest request) {
        financingService.savePerformanceMetrics(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Performance metrics saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/funding-request-overview")
    public ResponseEntity<ApiResponse> saveFundingRequestOverview(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingFundingRequestOverviewRequest request) {
        financingService.saveFundingRequestOverview(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Funding request overview saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/budget-breakdown")
    public ResponseEntity<ApiResponse> saveBudgetBreakdown(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingBudgetBreakdownRequest request) {
        financingService.saveBudgetBreakdown(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Budget breakdown saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/revenue-outlook")
    public ResponseEntity<ApiResponse> saveRevenueOutlook(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingRevenueOutlookRequest request) {
        financingService.saveRevenueOutlook(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Revenue outlook saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/funding-history")
    public ResponseEntity<ApiResponse> saveFundingHistory(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingFundingHistoryRequest request) {
        financingService.saveFundingHistory(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Funding history saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/execution-readiness")
    public ResponseEntity<ApiResponse> saveExecutionReadiness(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingExecutionReadinessRequest request) {
        financingService.saveExecutionReadiness(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Execution readiness saved successfully")
                .build());
    }
    
    @PutMapping("/{financingId}/compliance-and-confirmation")
    public ResponseEntity<ApiResponse> saveComplianceAndConfirmation(
            @PathVariable String companyId,
            @PathVariable String financingId,
            @RequestBody FinancingComplianceAndConfirmationRequest request) {
        financingService.saveComplianceAndConfirmation(financingId, request);
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message("Compliance and confirmation saved successfully")
                .build());
    }
}
