package com.crawdwall_backend_api.financing.projectorganization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingPrimaryContact {
    private String directorName;
    private String role;
    private String email;
    private String phoneNumber;
    private String nationality;
    private String ownershipPercentage;
}
