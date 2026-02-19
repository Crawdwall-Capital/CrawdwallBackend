package com.crawdwall_backend_api.company.companyKycOne.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyKYC1CountResponse {

    private long totalCount;
    private long pendingCount;
    private long approvedCount;
    private long rejectedCount;
    private long inProcessCount;
    private long requestForChangeCount;
    
}
