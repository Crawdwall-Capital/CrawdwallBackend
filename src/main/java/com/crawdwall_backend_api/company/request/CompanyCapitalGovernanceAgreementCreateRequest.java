package com.crawdwall_backend_api.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record CompanyCapitalGovernanceAgreementCreateRequest(
    @JsonProperty(required = true) boolean consentToMilestoneBasedDisbursement,
    @JsonProperty(required = true) boolean consentToEscrowOrControlledAccount,
    @JsonProperty(required = true) boolean consentToThirdPartyMonitoring,
    @JsonProperty(required = true) boolean understandSuspensionPolicy,
    @JsonProperty(required = true) String digitalSignatureUrl,
    @JsonProperty(required = true) LocalDate agreementDate
) {
    
}
