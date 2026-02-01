package com.crawdwall_backend_api.utils;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("Pending"),
    ACTIVE("Active"),
    INACTIVE("Inactive"),
    BLOCKED("Blocked");


    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

}
