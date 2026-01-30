package com.crawdwall_backend_api.rolepermissionmgnt.request;

import com.crawdwall_backend_api.rolepermissionmgnt.BusinessManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.NomineeDirectorManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.PaymentManagement;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

import java.util.Set;

@Builder
public record RoleUpdateRequest(
    @JsonProperty(required = true) String name,
    @JsonProperty(required = true) String description,
    @JsonProperty(required = true) Set<BusinessManagement> businessManagementPermissions,
    @JsonProperty(required = true) Set<NomineeDirectorManagement> nomineeDirectorManagementPermissions,
    @JsonProperty(required = true) Set<PaymentManagement> paymentManagementPermissions
) {


}
