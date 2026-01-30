package com.crawdwall_backend_api.admin.request;

import lombok.Builder;

@Builder
public record AdminFilterRequest(
    String firstName,
    String lastName,
    String emailAddress,
    String roleName
) {
    
}
