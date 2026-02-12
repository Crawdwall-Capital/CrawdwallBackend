package com.crawdwall_backend_api.financing.request;

public record FinancingVendorsInfrastructureRequest(
    String keyOperationalRisks,
    String dependenciesBeforeExecution,
    Boolean productionVendorConfirmed,
    Boolean venueInfrastructureVendorConfirmed,
    Boolean logisticsVendorConfirmed,
    Boolean technologyVendorConfirmed,
    Boolean marketingVendorConfirmed,
    String vendorConstraints
) {}
