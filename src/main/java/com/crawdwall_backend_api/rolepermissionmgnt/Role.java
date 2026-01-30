package com.crawdwall_backend_api.rolepermissionmgnt;


import com.crawdwall_backend_api.utils.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role extends BaseEntity {
    @NotBlank
    @Indexed(unique = true)
    private String name;
    private String description;
    private boolean isDeleted;
    private boolean isActive;

    private Set<BusinessManagement> businessManagementPermissions;
    private Set<NomineeDirectorManagement> nomineeDirectorManagementPermissions;
    private Set<PaymentManagement> paymentManagementPermissions;
 
}
