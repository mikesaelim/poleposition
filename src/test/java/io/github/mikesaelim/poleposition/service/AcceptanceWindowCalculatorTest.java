package io.github.mikesaelim.poleposition.service;

import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AcceptanceWindowCalculatorTest {

    private AcceptanceWindowCalculator calculator = new AcceptanceWindowCalculator();

    @Test
    public void testAcceptanceWindowFor_Tuesday() throws Exception {
        LocalDate tuesday = LocalDate.of(2016, 8, 16);
        AcceptanceWindow window = calculator.acceptanceWindowFor(tuesday);

        assertEquals(ZonedDateTime.of(2016, 8, 16, 20, 0, 0, 0, ZoneOffset.UTC), window.getStart());
        assertEquals(ZonedDateTime.of(2016, 8, 17, 20, 0, 0, 0, ZoneOffset.UTC), window.getEnd());
        assertEquals(tuesday, window.getSubmissionDate());
    }

    @Test
    public void testAcceptanceWindowFor_Friday() throws Exception {
        LocalDate friday = LocalDate.of(2016, 8, 19);
        AcceptanceWindow window = calculator.acceptanceWindowFor(friday);

        assertEquals(ZonedDateTime.of(2016, 8, 19, 20, 0, 0, 0, ZoneOffset.UTC), window.getStart());
        assertEquals(ZonedDateTime.of(2016, 8, 22, 20, 0, 0, 0, ZoneOffset.UTC), window.getEnd());
        assertEquals(friday, window.getSubmissionDate());
    }

    @Test
    public void testAcceptanceWindowFor_Saturday() throws Exception {
        LocalDate saturday = LocalDate.of(2016, 8, 20);
        AcceptanceWindow window = calculator.acceptanceWindowFor(saturday);

        assertNull(window);
    }

    @Test
    public void testAcceptanceWindowFor_Sunday() throws Exception {
        LocalDate sunday = LocalDate.of(2016, 8, 21);
        AcceptanceWindow window = calculator.acceptanceWindowFor(sunday);

        assertNull(window);
    }

    @Test
    public void testAcceptanceWindowFor_Holiday() throws Exception {
        LocalDate weekday = LocalDate.of(2016, 7, 4);
        AcceptanceWindow window = calculator.acceptanceWindowFor(weekday);

        assertEquals(ZonedDateTime.of(2016, 7, 4, 20, 0, 0, 0, ZoneOffset.UTC), window.getStart());
        assertEquals(ZonedDateTime.of(2016, 7, 5, 20, 0, 0, 0, ZoneOffset.UTC), window.getEnd());
        assertEquals(weekday, window.getSubmissionDate());
    }

}