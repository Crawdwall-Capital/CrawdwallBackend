package com.crawdwall_backend_api.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyCapitalGovernanceAgreement {
    private boolean consentToMilestoneBasedDisbursement;
    private boolean consentToEscrowOrControlledAccount;
    private boolean consentToThirdPartyMonitoring;
    private boolean understandSuspensionPolicy;
    private String digitalSignatureUrl;
    private LocalDate agreementDate;
}
