package com.crawdwall_backend_api.company.response;

import com.crawdwall_backend_api.utils.Address;
import com.crawdwall_backend_api.company.CompanyType;
import com.crawdwall_backend_api.utils.Status;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {

    String id;
    String companyName;
    String companyEmail;
    String companyPhone;
    String companyWebsite;
    String companyLogo;
    String companyRegistrationNumber;
    String companyRegistrationDate;
    CompanyType companyType;
    String userId;
    Address companyAddress;
    boolean isActive;
    boolean isDeleted;
    boolean isVerified;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Status status;
    
}
