package io.github.mikesaelim.poleposition.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

/**
 * Calculates the acceptance windows for given sets of days.
 *
 * Even though these methods could have been made static, I made this a service so it can easily be mocked out in
 * testing.
 */
@Service
public class AcceptanceWindowCalculator {

    private static LocalTime SUBMISSION_DEADLINE = LocalTime.of(16, 0);

    /**
     * Calculate the acceptance window starting on a given day.
     *
     * Right now there is no logic concerning weekends and holidays.
     *
     * @param date any date
     * @return acceptance window starting at 4pm ET and ending on the next day at 4pm ET
     */
    public AcceptanceWindow acceptanceWindowFor(LocalDate date) {
        ZonedDateTime thisSubmissionDeadline = date.atTime(SUBMISSION_DEADLINE)
                .atZone(AcceptanceWindow.EASTERN_TIME);
        ZonedDateTime nextSubmissionDeadline = date.plusDays(1).atTime(SUBMISSION_DEADLINE)
                .atZone(AcceptanceWindow.EASTERN_TIME);

        return new AcceptanceWindow(thisSubmissionDeadline, nextSubmissionDeadline);
    }



}
