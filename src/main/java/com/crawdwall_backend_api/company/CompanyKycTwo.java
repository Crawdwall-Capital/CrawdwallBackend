package com.crawdwall_backend_api.company;

import com.crawdwall_backend_api.utils.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "company_kyc_two")
@EqualsAndHashCode(callSuper = true)
public class CompanyKycTwo extends BaseEntity{

    @Indexed(unique = true)
   private String companyId;
}
