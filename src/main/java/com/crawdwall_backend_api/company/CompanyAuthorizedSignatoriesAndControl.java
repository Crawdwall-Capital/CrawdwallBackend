package com.crawdwall_backend_api.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAuthorizedSignatoriesAndControl {
    private String signatoryName;
    private String role;
    private String email;
    private String phoneNumber;
    private boolean isPrimary;
    private String governmentIdDocumentUrl;
    private String authorizationLetterUrl;
}