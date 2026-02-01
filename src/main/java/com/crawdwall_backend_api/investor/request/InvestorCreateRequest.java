package com.crawdwall_backend_api.investor.request;

import com.crawdwall_backend_api.investor.InvestorType;
import com.crawdwall_backend_api.investor.RiskTolerance;
import com.crawdwall_backend_api.investor.InvestmentExperience;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.time.LocalDate;
import java.math.BigDecimal;

@Builder
public record InvestorCreateRequest(
    
    @JsonProperty(required = true) String firstName,
    @JsonProperty(required = true) String lastName,
    @JsonProperty(required = true) String investorEmail,
    @JsonProperty(required = true) String investorPhone,
    @JsonProperty(required = true) LocalDate dateOfBirth,
    @JsonProperty(required = true) String nationality,
    String profilePicture,
    
    @JsonProperty(required = true) String investorNationalId,
    @JsonProperty(required = true) String investorTaxId,
    
    @JsonProperty(required = true) InvestorType investorType,
    @JsonProperty(required = true) RiskTolerance riskTolerance,
    @JsonProperty(required = true) InvestmentExperience investmentExperience,
    BigDecimal minimumInvestmentAmount,
    BigDecimal maximumInvestmentAmount,
    
    @JsonProperty(required = true) BigDecimal annualIncome,
    @JsonProperty(required = true) BigDecimal netWorth,
    BigDecimal liquidAssets,
    

    @JsonProperty(required = true) String password
) {
    
}