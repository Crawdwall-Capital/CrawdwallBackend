package com.crawdwall_backend_api.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValueExistanceCheckResponse {
    private boolean exists;
    private String message;
}

