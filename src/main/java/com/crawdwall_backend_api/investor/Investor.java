package com.crawdwall_backend_api.investor;

import com.crawdwall_backend_api.utils.BaseEntity;
import com.crawdwall_backend_api.utils.Address;
import com.crawdwall_backend_api.utils.Status;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "investors")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Investor extends BaseEntity {
    private String firstName;
    private String lastName;
    
    @Indexed(unique = true)
    private String investorEmail;
    
    @Indexed(unique = true)
    private String investorPhone;
    private LocalDate dateOfBirth;
    private String nationality;
    private String profilePicture;
    
    @Indexed(unique = true)
    private String investorNationalId;
    
    @Indexed(unique = true)
    private String investorTaxId;
    
    private InvestorType investorType;
    private RiskTolerance riskTolerance;
    private InvestmentExperience investmentExperience;
    private BigDecimal minimumInvestmentAmount;
    private BigDecimal maximumInvestmentAmount;
    
    private BigDecimal annualIncome;
    private BigDecimal netWorth;
    private BigDecimal liquidAssets;
    
    private Address investorAddress;
    
    private boolean isActive;
    private boolean isDeleted;
    private boolean isVerified;
    private LocalDateTime verifiedAt;
    private String userId;
    private Status status;

    
}