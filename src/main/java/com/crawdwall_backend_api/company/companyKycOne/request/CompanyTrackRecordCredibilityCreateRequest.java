package com.crawdwall_backend_api.company.companyKycOne.request;

import com.crawdwall_backend_api.company.TrackRecordCredibilityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.validation.constraints.NotNull;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyTrackRecordCredibilityCreateRequest {
    @NotNull
    private TrackRecordCredibilityType trackRecordCredibilityType;
    
    @NotNull
    private Long numberOfMajorProjectsDelivered;
    
    @NotNull
    private Long yearsOfExperience;
    
    @NotNull
    private Set<String> majorProjectsDeliveredSocialMediaLinks;
    
    @NotNull
    private BigDecimal averageProjectExcutedAmount;
    
    @NotNull
    private String majorSponsorPartnerNames;
}