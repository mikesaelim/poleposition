package io.github.mikesaelim.poleposition.service;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Utilities for time format conversion
 */
public class TimeUtils {

    // Conversions between ZonedDateTime and Timestamp

    public static Timestamp convertToUtcTimestamp(ZonedDateTime zonedDateTime) {
        return Timestamp.valueOf(zonedDateTime.withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }

    public static ZonedDateTime convertFromUtcTimestamp(Timestamp timestamp) {
        return ZonedDateTime.of(timestamp.toLocalDateTime(), ZoneOffset.UTC);
    }

}
