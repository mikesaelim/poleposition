package io.github.mikesaelim.poleposition.service;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;

/**
 * Calculates the acceptance windows for given sets of days.
 *
 * Even though these methods could have been made static, I made this a service so it can easily be mocked out in
 * testing.
 */
@Service
public class AcceptanceWindowCalculator {

    private static final LocalTime SUBMISSION_DEADLINE = LocalTime.of(16, 0);

    /**
     * Calculate the acceptance window starting on a given day.  Returns {@literal null} if there isn't one.
     *
     * Right now there is no logic concerning holidays.
     *
     * @param date any date
     * @return acceptance window starting at 4pm ET and ending on the next day at 4pm ET
     */
    public AcceptanceWindow acceptanceWindowFor(LocalDate date) {
        if (isWeekend(date)) return null;

        ZonedDateTime thisSubmissionDeadline = date.atTime(SUBMISSION_DEADLINE)
                .atZone(AcceptanceWindow.EASTERN_TIME);

        LocalDate nextSubmissionDay = date.plusDays(1);
        while (isWeekend(nextSubmissionDay)) {
            nextSubmissionDay = nextSubmissionDay.plusDays(1);
        }

        ZonedDateTime nextSubmissionDeadline = nextSubmissionDay.atTime(SUBMISSION_DEADLINE)
                .atZone(AcceptanceWindow.EASTERN_TIME);

        return new AcceptanceWindow(thisSubmissionDeadline, nextSubmissionDeadline);
    }

    private static boolean isWeekend(LocalDate d) {
        DayOfWeek day = d.getDayOfWeek();
        return SATURDAY.equals(day) || SUNDAY.equals(day);
    }

}
