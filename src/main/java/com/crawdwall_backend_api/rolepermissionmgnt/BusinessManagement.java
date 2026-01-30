package com.crawdwall_backend_api.rolepermissionmgnt;



import com.crawdwall_backend_api.utils.exception.InvalidInputException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum BusinessManagement {

    VIEW_BUSINESS_LIST("View Business List"),
    CREATE_EDIT_BUSINESS("Create Business"),
    DEACTIVATE_DELETE_BUSINESS("Deactivate/Delete Business");

    private final String displayName;
    BusinessManagement(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }


    public static Set<BusinessManagement> getAllPermissions() {
        return Set.of(values());
    }

    public static Set<String> getAllDisplayNames() {
        return Arrays.stream(values())
                .map(BusinessManagement::getDisplayName)
                .collect(Collectors.toSet());
    }

    public static BusinessManagement fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(permission -> permission.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Unknown permission name: " + displayName));
    }
}
