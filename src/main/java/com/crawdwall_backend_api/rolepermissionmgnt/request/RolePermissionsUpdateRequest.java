package com.crawdwall_backend_api.rolepermissionmgnt.request;


import com.crawdwall_backend_api.rolepermissionmgnt.BusinessManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.NomineeDirectorManagement;
import com.crawdwall_backend_api.rolepermissionmgnt.PaymentManagement;
import lombok.Builder;

import java.util.Set;

@Builder
public record RolePermissionsUpdateRequest(

        Set<BusinessManagement> businessManagementPermissions,
        Set<NomineeDirectorManagement> nomineeDirectorManagementPermissions,
        Set<PaymentManagement> paymentManagementPermissions
) {
}
