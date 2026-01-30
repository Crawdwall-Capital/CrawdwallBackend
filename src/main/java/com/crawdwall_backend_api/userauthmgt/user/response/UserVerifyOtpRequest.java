package com.crawdwall_backend_api.userauthmgt.user.response;

import com.crawdwall_backend_api.userauthmgt.userotp.UserOtpType;
import com.fasterxml.jackson.annotation.JsonProperty;


public record UserVerifyOtpRequest(
   @JsonProperty(required = true) String otp,
   @JsonProperty(required = true)  String userEmail,
   @JsonProperty(required = true) UserOtpType userOtpType
) {
}
