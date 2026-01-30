package com.crawdwall_backend_api.userauthmgt.user.response;


import com.crawdwall_backend_api.userauthmgt.user.User;
import lombok.Builder;

@Builder
public record UserProfileResponse(

        String userId,
        String firstName,
        String lastName,
        String emailAddress,
        String phoneNumber,
        String profilePictureUrl,
        String profileColorCode
) {

    public static UserProfileResponse fromUser(User user) {
        return UserProfileResponse.builder()
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailAddress(user.getEmailAddress())
                .phoneNumber(user.getPhoneNumber())
                .profilePictureUrl(user.getProfilePictureUrl())
           
                .build();
    }

}
