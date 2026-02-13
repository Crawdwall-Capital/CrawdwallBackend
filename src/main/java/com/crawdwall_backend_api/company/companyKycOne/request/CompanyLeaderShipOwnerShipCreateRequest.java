package com.crawdwall_backend_api.company.companyKycOne.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyLeaderShipOwnerShipCreateRequest {
    
    private Set<CompanyLeaderShipOwnerShipSetUpRequest> companyLeaderShipOwnerShipSetUpRequest;
}
