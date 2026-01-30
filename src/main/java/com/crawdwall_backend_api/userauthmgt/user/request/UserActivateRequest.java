package com.crawdwall_backend_api.userauthmgt.user.request;

public record UserActivateRequest(

        String otp,
        String userId
) {
}
