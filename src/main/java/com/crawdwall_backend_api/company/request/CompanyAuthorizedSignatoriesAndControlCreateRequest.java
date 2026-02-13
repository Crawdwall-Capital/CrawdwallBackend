package com.crawdwall_backend_api.company.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.util.Set;

@Builder
public record CompanyAuthorizedSignatoriesAndControlCreateRequest(
    @JsonProperty(required = true) CompanySignatoryRequest primarySignatory,
    Set<CompanySignatoryRequest> secondarySignatories,
    String governmentIdDocumentUrl,
    String authorizationLetterUrl
) {
    
}