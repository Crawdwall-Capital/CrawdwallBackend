package com.crawdwall_backend_api.userauthmgt.user.response;


import com.crawdwall_backend_api.userauthmgt.user.UserType;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record UserCreateResponse(
        String userId,
        String firstName,
        String lastName,
        String emailAddress,
        String phoneNumber,
        UserType userType,
        String profilePictureUrl,
        LocalDate dateOfBirth,
        boolean isActive,
        boolean isBlocked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String otp,
        boolean isVerified
) {
}
