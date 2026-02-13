package com.crawdwall_backend_api.company.companyKycOne.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentEntityValueCreateRequestSetUp {

    @NotNull
    private   DocumentEntityValueCreateRequest taxIdentificationDocumentCreateRequest;
    @NotNull
    private   DocumentEntityValueCreateRequest proofOfAddressDocumentCreateRequest;
    @NotNull
    private   DocumentEntityValueCreateRequest governmentIdDocumentCreateRequest;
    @NotNull
    private   DocumentEntityValueCreateRequest complianceAndIdentityDocumentCreateRequest;
    @NotNull
    private   DocumentEntityValueCreateRequest certificateOfIncorporationCreateRequest;
    
}
