package com.crawdwall_backend_api.investor.response;

import com.crawdwall_backend_api.utils.Address;
import com.crawdwall_backend_api.utils.Status;
import com.crawdwall_backend_api.investor.InvestorType;
import com.crawdwall_backend_api.investor.RiskTolerance;
import com.crawdwall_backend_api.investor.InvestmentExperience;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvestorResponse {
    String id;
    String firstName;
    String lastName;
    String investorEmail;
    String investorPhone;
    LocalDate dateOfBirth;
    String nationality;
    String profilePicture;

    String investorNationalId;
    String investorTaxId;
    
    InvestorType investorType;
    RiskTolerance riskTolerance;
    InvestmentExperience investmentExperience;
    BigDecimal minimumInvestmentAmount;
    BigDecimal maximumInvestmentAmount;
    
    BigDecimal annualIncome;
    BigDecimal netWorth;
    BigDecimal liquidAssets;
    Address investorAddress;
    String userId;
    boolean isActive;
    boolean isDeleted;
    boolean isVerified;
    LocalDateTime verifiedAt;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}