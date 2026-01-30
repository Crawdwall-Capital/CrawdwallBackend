package com.crawdwall_backend_api.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

public static final String DATE_FORMAT_ONE = "dd MMMM yyyy";


    /**
     * Formats a given `LocalDateTime` object into a string representation based on the specified date format.
     *
     * @param dateTime   the `LocalDateTime` object to format; must not be null
     * @param dateFormat the desired date format pattern
     * @return the formatted date-time string
     * @throws IllegalArgumentException if the `dateTime` parameter is null
     */
    public static String formatLocalDateTime(LocalDateTime dateTime, String dateFormat) {
        if (dateTime == null) {
            throw new IllegalArgumentException("dateTime cannot be null");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return dateTime.format(formatter);
    }
}
