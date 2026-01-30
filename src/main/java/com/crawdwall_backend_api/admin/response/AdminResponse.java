package com.crawdwall_backend_api.admin.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AdminResponse(
    String adminId,
    String userId,
    String firstName,
    String lastName,
    String emailAddress,
    String phoneNumber,
    String profilePictureUrl,
    LocalDateTime createdAt,
    boolean isVerified,
    boolean isActive,
    String roleName
) {
    
}
