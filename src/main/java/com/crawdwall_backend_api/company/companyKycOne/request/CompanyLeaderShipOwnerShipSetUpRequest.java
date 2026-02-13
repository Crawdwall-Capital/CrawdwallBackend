package com.crawdwall_backend_api.company.companyKycOne.request;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonProperty;
@Builder
public record CompanyLeaderShipOwnerShipSetUpRequest(

    @JsonProperty(required = true) String companyAdminName,
    @JsonProperty(required = true) String companyAdminTitle,
    @JsonProperty(required = true) String companyAdminEmail,
    @JsonProperty(required = true) String companyAdminPhone,
    @JsonProperty(required = true) String companyAdminNationality,
    @JsonProperty(required = true) String companyAdminRole,
    @JsonProperty(required = true) String levelOfControl,
    boolean isMainFounder
    
    )
    

{

}