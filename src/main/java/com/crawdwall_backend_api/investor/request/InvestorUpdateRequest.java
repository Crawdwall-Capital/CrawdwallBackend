package com.crawdwall_backend_api.investor.request;

import com.crawdwall_backend_api.investor.InvestorType;
import com.crawdwall_backend_api.investor.RiskTolerance;
import com.crawdwall_backend_api.investor.InvestmentExperience;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import java.time.LocalDate;
import java.math.BigDecimal;

@Builder
public record InvestorUpdateRequest(
    String firstName,
    String lastName,
    String investorEmail,
    String investorPhone,
    LocalDate dateOfBirth,
    String nationality,
    String profilePicture,
    
    String investorNationalId,
    String investorTaxId,
    
    InvestorType investorType,
    RiskTolerance riskTolerance,
    InvestmentExperience investmentExperience,
    BigDecimal minimumInvestmentAmount,
    BigDecimal maximumInvestmentAmount,
    
    BigDecimal annualIncome,
    BigDecimal netWorth,
    BigDecimal liquidAssets
) {
    
}