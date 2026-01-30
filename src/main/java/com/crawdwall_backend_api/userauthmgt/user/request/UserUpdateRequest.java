package com.crawdwall_backend_api.userauthmgt.user.request;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserUpdateRequest (

        String firstName,
      String lastName,
      LocalDate dateOfBirth,
      String phoneNumber,
      String profilePictureUrl
) {
}