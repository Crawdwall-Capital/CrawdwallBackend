package com.crawdwall_backend_api.admin;


import com.crawdwall_backend_api.utils.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "admin")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Admin extends BaseEntity {
    @Indexed
    private String userId;
    private String roleId;
    private boolean isActive;
    private boolean isDeleted;
    private boolean isDefault;
    private boolean isVerified;
    private LocalDateTime verifiedAt;

    
}
