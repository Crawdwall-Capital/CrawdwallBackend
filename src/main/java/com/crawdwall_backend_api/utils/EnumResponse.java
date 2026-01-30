package com.crawdwall_backend_api.utils;

public record EnumResponse<T>(
        T value,
        String displayName
) {

}
