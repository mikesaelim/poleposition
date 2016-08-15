package io.github.mikesaelim.poleposition.service;

import lombok.Value;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * The acceptance window of a day D is defined as the interval starting D's submission deadline (4pm ET) and ending at
 * the next submission deadline (usually 4pm ET on day D+1 unless D+1 falls on a weekend or holiday).  Papers submitted
 * during this window are made available on arXiv the night of D+1, and appear on the email digest on the morning of
 * day D+2.
 *
 * Start and end times are always returned in UTC from this object.
 */
@Value
public class AcceptanceWindow {

    public static ZoneId EASTERN_TIME = ZoneId.of("America/New_York");

    private ZonedDateTime start;
    private ZonedDateTime end;

    public AcceptanceWindow(ZonedDateTime start, ZonedDateTime end) {
        this.start = start.withZoneSameInstant(ZoneOffset.UTC);
        this.end = end.withZoneSameInstant(ZoneOffset.UTC);
    }

    /**
     * Return the submission date, in the ET time zone.
     */
    public LocalDate getSubmissionDate() {
        return start.withZoneSameInstant(EASTERN_TIME).toLocalDate();
    }

}
