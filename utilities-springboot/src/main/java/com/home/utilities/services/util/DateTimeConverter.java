package com.home.utilities.services.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateTimeConverter {

    public static <T> String convertDateToString(final T dateType, final DateTimeFormatter dateTimeFormatter) {
        if (dateType == null || dateTimeFormatter == null) {
            return null;
        }
        if (dateType instanceof LocalDate) {
            return ((LocalDate) dateType).atStartOfDay(ZoneId.systemDefault()).format(dateTimeFormatter);
        } else if (dateType instanceof Instant) {
            return ((Instant) dateType).atZone(ZoneId.systemDefault()).format(dateTimeFormatter);
        } else {
            return null;
        }
    }

    public static boolean isDateInBetween(final LocalDate date, final LocalDate min, final LocalDate max) {
        return !(date.isBefore(min) || date.isAfter(max));
    }

    public static String fromInstantToString(final Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
    }

    public static String toTime(final Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
}
