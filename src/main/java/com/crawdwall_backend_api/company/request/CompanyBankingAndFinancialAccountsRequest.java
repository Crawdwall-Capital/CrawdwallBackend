package com.crawdwall_backend_api.company.request;

import com.crawdwall_backend_api.company.CompanyAccountCurrency;
import com.crawdwall_backend_api.company.CompanyAccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CompanyBankingAndFinancialAccountsRequest(
    @JsonProperty(required = true) String bankName,
    @JsonProperty(required = true) String accountName,
    @JsonProperty(required = true) String accountNumber,
    @JsonProperty(required = true) CompanyAccountType accountType,
    @JsonProperty(required = true) CompanyAccountCurrency accountCurrency,
    String letterOfStatementUrl
) {
    
}