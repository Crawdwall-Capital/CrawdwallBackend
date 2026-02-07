package com.crawdwall_backend_api.company;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CompanyType {
    
    EVENT_COMPANY("Event company"),
    CORPORATE_ENTERPRISE("Corporate/enterprise"),
    CREATIVE_STUDIO("Creative studio"),
    NGO_NONPROFIT("NGO/Nonprofit"),
    INDIVIDUAL_LED_PROJECT("Individual-Led Project");
    
    private final String displayName;
    
    CompanyType(String displayName) {
        this.displayName = displayName;
    }
    
    @JsonValue
    public String getDisplayName() {
        return displayName;
    }
}
