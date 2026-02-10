package com.crawdwall_backend_api.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyExecutionAndReportingReadiness {
    // Internal Finance Lead / Contact Person
    private String fullName;
    private String role;
    private String email;
    private String phoneNumber;
    
    // Accounting and Reporting
    private CompanyAccountSystem accountingSystemUsed;
    private CompanyFinancialReporting financialReportingFrequency;
    
    // Escrow / Third-Party Governance
    private boolean hasPastEscrowUse;
    private Set<CompanyTypeOfArrangement> typeOfArrangement;
    private String purposeOfEscrow;
    private String counterpartyPlatformUsed;
    private CompanyDurationOfAgreement durationOfAgreement;
}