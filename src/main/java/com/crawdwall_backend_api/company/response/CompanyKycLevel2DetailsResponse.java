package com.crawdwall_backend_api.company.response;

import com.crawdwall_backend_api.company.CompanyBankingAndFinancialAccounts;
import com.crawdwall_backend_api.company.CompanyAuthorizedSignatoriesAndControl;
import com.crawdwall_backend_api.company.CompanyFinancialIntegrityAndRiskControl;
import com.crawdwall_backend_api.company.CompanyExecutionAndReportingReadiness;
import com.crawdwall_backend_api.company.CompanyCapitalGovernanceAgreement;
import lombok.Builder;

import java.util.Set;

@Builder
public record CompanyKycLevel2DetailsResponse(
    Set<CompanyBankingAndFinancialAccounts> bankingAndFinancialAccounts,
    
    Set<CompanyAuthorizedSignatoriesAndControl> authorizedSignatoriesAndControl,
    
    CompanyFinancialIntegrityAndRiskControl financialIntegrityAndRiskControl,
    
    CompanyExecutionAndReportingReadiness executionAndReportingReadiness,
    
    CompanyCapitalGovernanceAgreement capitalGovernanceAgreement
) {}
