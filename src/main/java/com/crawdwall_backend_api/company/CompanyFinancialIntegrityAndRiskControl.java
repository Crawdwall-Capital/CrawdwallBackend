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
public class CompanyFinancialIntegrityAndRiskControl {
    private Set<CompanyRevenueSource> primaryRevenueSources;
    private CompanyTransactionVolume expectedTransactionVolume;
    private Set<CompanyFundsDeclaration> sourceOfFundsDeclaration;
    
    // PEP (Politically Exposed Person) fields
    private boolean isPoliticallyExposedPerson;
    private String pepRole;
    private String pepCountry;
    private Integer pepYear;
    
    // Litigation/Bankruptcy/Insolvency fields
    private boolean hasLitigationBankruptcyOrInsolvency;
    private String litigationNature;
    private Integer litigationYear;
    private String litigationCurrentStatus;
    
    // Sanctions fields
    private boolean isSubjectToSanctions;
    private String sanctionsPartyAffected;
    private String sanctionsNature;
    private String sanctionsCurrentStatus;
}