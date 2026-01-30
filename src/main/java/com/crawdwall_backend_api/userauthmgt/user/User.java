package com.crawdwall_backend_api.userauthmgt.user;


import com.crawdwall_backend_api.utils.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity {
    private String firstName;
    private String lastName;
    @Indexed
    private String emailAddress;
    private String password;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private UserType userType;
    private boolean isActive;
    private String profilePictureUrl;
    private String profileColorCode;
    private boolean isDeleted;
    private boolean isVerified;
    private LocalDateTime verifiedAt;
    private LocalDateTime lastLoginAt;
    private LocalDateTime deletedAt;
    private LocalDateTime passwordChangedAt;

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, UserType userType,
                boolean isActive, String profilePictureUrl, String profileColorCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.isActive = isActive;
        this.profilePictureUrl = profilePictureUrl;
        this.profileColorCode = profileColorCode;
    }

    public User(String firstName, String lastName, String emailAddress, String phoneNumber, UserType userType,
                boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.isActive = isActive;

    }
}
