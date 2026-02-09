package com.crawdwall_backend_api.company.response;

import com.crawdwall_backend_api.userauthmgt.user.response.UserResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyAuthResponse {
    
    String token;
    String refreshToken;
    String companyId;
    String userId;
    boolean kycCompleted;
    boolean documentVerified;
    CompanyResponse companyResponse;
    UserResponse userResponse;
}
