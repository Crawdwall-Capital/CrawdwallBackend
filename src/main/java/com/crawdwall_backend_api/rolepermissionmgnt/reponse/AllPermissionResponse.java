package com.crawdwall_backend_api.rolepermissionmgnt.reponse;

import com.crawdwall_backend_api.rolepermissionmgnt.BusinessManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.NomineeDirectorManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.PaymentManagement;
import lombok.Builder;

import java.util.Set;

@Builder
public record AllPermissionResponse(
    Set<BusinessManagement> businessManagementPermissions,
    Set<NomineeDirectorManagement> nomineeDirectorManagementPermissions,
    Set<PaymentManagement> paymentManagementPermissions
) {
    
}
