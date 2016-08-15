package io.github.mikesaelim.poleposition.service;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;

public class AcceptanceWindowCalculatorTest {

    private AcceptanceWindowCalculator calculator = new AcceptanceWindowCalculator();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAcceptanceWindowFor_Weekday() throws Exception {
        LocalDate weekday = LocalDate.of(2016, 8, 16);
        AcceptanceWindow window = calculator.acceptanceWindowFor(weekday);

        assertEquals(ZonedDateTime.of(2016, 8, 16, 20, 0, 0, 0, ZoneOffset.UTC), window.getStart());
        assertEquals(ZonedDateTime.of(2016, 8, 17, 20, 0, 0, 0, ZoneOffset.UTC), window.getEnd());
        assertEquals(weekday, window.getSubmissionDate());
    }

    @Test
    public void testAcceptanceWindowFor_Weekend() throws Exception {
        LocalDate weekday = LocalDate.of(2016, 8, 13);
        AcceptanceWindow window = calculator.acceptanceWindowFor(weekday);

        assertEquals(ZonedDateTime.of(2016, 8, 13, 20, 0, 0, 0, ZoneOffset.UTC), window.getStart());
        assertEquals(ZonedDateTime.of(2016, 8, 14, 20, 0, 0, 0, ZoneOffset.UTC), window.getEnd());
        assertEquals(weekday, window.getSubmissionDate());
    }

    @Test
    public void testAcceptanceWidnowFor_Holiday() throws Exception {
        LocalDate weekday = LocalDate.of(2016, 7, 4);
        AcceptanceWindow window = calculator.acceptanceWindowFor(weekday);

        assertEquals(ZonedDateTime.of(2016, 7, 4, 20, 0, 0, 0, ZoneOffset.UTC), window.getStart());
        assertEquals(ZonedDateTime.of(2016, 7, 5, 20, 0, 0, 0, ZoneOffset.UTC), window.getEnd());
        assertEquals(weekday, window.getSubmissionDate());
    }

}