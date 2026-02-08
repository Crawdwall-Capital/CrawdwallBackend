package com.crawdwall_backend_api.company;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyLeaderShipOwnerShip {
   
  private String companyAdminName;
  private String companyAdminEmail;
  private String companyAdminPhone;
  private String companyAdminNationality;
  private String companyAdminRole;
  private String levelOfControl;
  private boolean isMainFounder;
}
