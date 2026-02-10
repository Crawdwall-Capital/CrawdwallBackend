package com.crawdwall_backend_api.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyBankingAndFinancialAccounts {
   
    private String bankName;
    private String accountName;
    private String accountNumber;
    private CompanyAccountType accountType;
    private CompanyAccountCurrency accountCurrency;
    private String letterOfStatementUrl;
}
