package com.crawdwall_backend_api.financing.request;

public record PrimaryContactRequest(
    String directorName,
    String role,
    String email,
    String phoneNumber,
    String nationality,
    String ownershipPercentage
) {}
