package com.crawdwall_backend_api.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record CompanyCapitalGovernanceAgreementCreateRequest(
    boolean consentToMilestoneBasedDisbursement,
    boolean consentToEscrowOrControlledAccount,
    boolean consentToThirdPartyMonitoring,
    boolean understandSuspensionPolicy,
    @JsonProperty(required = true) String digitalSignatureUrl,
    @JsonProperty(required = true) LocalDate agreementDate
) {
    
}
