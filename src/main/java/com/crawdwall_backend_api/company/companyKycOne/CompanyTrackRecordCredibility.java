package com.crawdwall_backend_api.company.companyKycOne;

import java.math.BigDecimal;
import java.util.Set;

import com.crawdwall_backend_api.company.TrackRecordCredibilityType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyTrackRecordCredibility {

    private TrackRecordCredibilityType trackRecordCredibilityType;
    private long numberOfMajorProjectsDelivered;
    private long yearsOfExperience;
    private Set<String> majorProjectsDeliveredSocialMediaLinks;
    private BigDecimal averageProjectExcutedAmount;
    private String majorSponsorPartnerNames;
    
}
