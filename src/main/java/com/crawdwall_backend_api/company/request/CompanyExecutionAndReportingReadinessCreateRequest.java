package com.crawdwall_backend_api.company.request;

import com.crawdwall_backend_api.company.CompanyAccountSystem;
import com.crawdwall_backend_api.company.CompanyFinancialReporting;
import com.crawdwall_backend_api.company.CompanyTypeOfArrangement;
import com.crawdwall_backend_api.company.CompanyDurationOfAgreement;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Set;

@Builder
public record CompanyExecutionAndReportingReadinessCreateRequest(
    // Internal Finance Lead / Contact Person
    @JsonProperty(required = true) String fullName,
    @JsonProperty(required = true) String role,
    @JsonProperty(required = true) String email,
    @JsonProperty(required = true) String phoneNumber,
    
    // Accounting and Reporting
    @JsonProperty(required = true) CompanyAccountSystem accountingSystemUsed,
    @JsonProperty(required = true) CompanyFinancialReporting financialReportingFrequency,
    
    // Escrow / Third-Party Governance
    @JsonProperty(required = true) boolean hasPastEscrowUse,
    Set<CompanyTypeOfArrangement> typeOfArrangement,
    String purposeOfEscrow,
    String counterpartyPlatformUsed,
    CompanyDurationOfAgreement durationOfAgreement
) {
    
}
