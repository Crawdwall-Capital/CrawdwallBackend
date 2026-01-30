package com.crawdwall_backend_api.userauthmgt.user.response;

import lombok.Builder;

@Builder
public record UserForgotPasswordResponse(

        String userId
) {
}
