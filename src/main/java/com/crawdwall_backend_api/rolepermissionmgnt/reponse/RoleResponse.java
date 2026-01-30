package com.crawdwall_backend_api.rolepermissionmgnt.reponse;

import com.crawdwall_backend_api.rolepermissionmgnt.BusinessManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.NomineeDirectorManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.PaymentManagement;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record RoleResponse(
        String id,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active,
        boolean isDeleted,
        @JsonProperty(required = true) String name,
        String description,
        Set<BusinessManagement> businessManagementPermissions,
        Set<NomineeDirectorManagement> nomineeDirectorManagementPermissions,
        Set<PaymentManagement> paymentManagementPermissions
) {
}
