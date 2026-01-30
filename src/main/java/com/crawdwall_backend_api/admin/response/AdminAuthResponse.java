package com.crawdwall_backend_api.admin.response;

import lombok.Builder;

@Builder
public record AdminAuthResponse(
    String token,
    String refreshToken,
    String adminId,
    String userId,
    AdminResponse adminResponse
) {
    
}
