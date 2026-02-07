package com.crawdwall_backend_api.utils;

import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class ValidationUtils {

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    // Phone validation pattern (basic international format)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[1-9]\\d{1,14}$"
    );

    // Website URL validation pattern
    private static final Pattern WEBSITE_PATTERN = Pattern.compile(
        "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}(/.*)?$"
    );

    private ValidationUtils() {
        // Utility class - prevent instantiation
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        return StringUtils.hasText(email) && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates phone number format
     */
    public static boolean isValidPhone(String phone) {
        return StringUtils.hasText(phone) && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates website URL format
     */
    public static boolean isValidWebsite(String website) {
        return StringUtils.hasText(website) && WEBSITE_PATTERN.matcher(website).matches();
    }

    /**
     * Validates that date is not in the future
     */
    public static boolean isValidPastDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now());
    }

    /**
     * Validates required string field
     */
    public static boolean isValidRequiredString(String value) {
        return StringUtils.hasText(value);
    }

    /**
     * Validates optional string field (can be null/empty but if present must have text)
     */
    public static boolean isValidOptionalString(String value) {
        return value == null || StringUtils.hasText(value);
    }
}