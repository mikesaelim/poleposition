package io.github.mikesaelim.poleposition.service;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.*;

public class TimeUtilsTest {

    @Test
    public void testConvertToUtcTimestamp() throws Exception {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 8, 14, 22, 0, 0, 0, ZoneId.of("America/Chicago"));
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2016, 8, 15, 3, 0, 0, 0));

        assertEquals(timestamp, TimeUtils.convertToUtcTimestamp(zonedDateTime));
    }

    @Test
    public void testConvertFromUtcTimestamp() throws Exception {
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2016, 8, 15, 3, 0, 0, 0));
        ZonedDateTime zonedDateTime = ZonedDateTime.of(2016, 8, 15, 3, 0, 0, 0, ZoneOffset.UTC);

        assertEquals(zonedDateTime, TimeUtils.convertFromUtcTimestamp(timestamp));
    }

}