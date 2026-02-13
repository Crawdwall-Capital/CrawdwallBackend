package com.crawdwall_backend_api.financing.executionoperationplan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinancingVendorsInfrastructure {
    private String keyOperationalRisks;
    private String dependenciesBeforeExecution;
    private Boolean productionVendorConfirmed;
    private Boolean venueInfrastructureVendorConfirmed;
    private Boolean logisticsVendorConfirmed;
    private Boolean technologyVendorConfirmed;
    private Boolean marketingVendorConfirmed;
    private String vendorConstraints;
}
