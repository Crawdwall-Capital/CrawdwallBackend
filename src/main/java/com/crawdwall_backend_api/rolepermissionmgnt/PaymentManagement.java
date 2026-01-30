package com.crawdwall_backend_api.rolepermissionmgnt;

import com.crawdwall_backend_api.utils.exception.InvalidInputException;


import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum PaymentManagement {

    VIEW_PAYOUT_LIST("View Payout List"),
    MANAGE_PAYOUTS("Manage Payouts"),
    VIEW_BUSINESS_INVOICES_AND_PAYMENT_HISTORY("View Business Invoices and Payment History"),
    MANAGE_BUSINESS_PAYMENTS("Manage Business Payments");

        private String displayName;

    PaymentManagement(String displayName) {
        this.displayName = displayName;
    }
    public String getDisplayName() {
        return displayName;
    }

    public static Set<PaymentManagement> getAllPermissions() {
        return Set.of(values());
    }

    public static Set<String> getAllDisplayNames() {
        return Arrays.stream(values())
                .map(PaymentManagement::getDisplayName)
                .collect(Collectors.toSet());
    }

    public static PaymentManagement fromDisplayName(String displayName) {
        return Arrays.stream(values())
                .filter(permission -> permission.getDisplayName().equals(displayName))
                .findFirst()
                .orElseThrow(() -> new InvalidInputException("Unknown permission name: " + displayName));
    }

}
