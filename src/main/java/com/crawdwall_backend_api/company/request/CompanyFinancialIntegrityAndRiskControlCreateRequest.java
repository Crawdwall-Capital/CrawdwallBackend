package com.crawdwall_backend_api.company.request;

import com.crawdwall_backend_api.company.CompanyRevenueSource;
import com.crawdwall_backend_api.company.CompanyTransactionVolume;
import com.crawdwall_backend_api.company.CompanyFundsDeclaration;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Set;

@Builder
public record CompanyFinancialIntegrityAndRiskControlCreateRequest(
    @JsonProperty(required = true) Set<CompanyRevenueSource> primaryRevenueSources,
    @JsonProperty(required = true) CompanyTransactionVolume expectedTransactionVolume,
    @JsonProperty(required = true) Set<CompanyFundsDeclaration> sourceOfFundsDeclaration,
    boolean isPoliticallyExposedPerson,
    String pepRole,
    String pepCountry,
    Integer pepYear,
    boolean hasLitigationBankruptcyOrInsolvency,
    String litigationNature,
    Integer litigationYear,
    String litigationCurrentStatus,
    boolean isSubjectToSanctions,
    String sanctionsPartyAffected,
    String sanctionsNature,
    String sanctionsCurrentStatus
) {
    
}
