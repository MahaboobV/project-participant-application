package com.nrc.project.participant.helper;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicationHelperTest {

    ApplicationHelper applicationHelper;

    @BeforeEach
    void setUp() {
        applicationHelper = new ApplicationHelper();
    }

    @Test
    void testISO8601DateFormattingDDMMYYYY() {
        String input = "08/11/2023"; // DD/MM/YYYY
        LocalDate expectedDate = LocalDate.of(2023, 11, 8);
        String result = applicationHelper.ISO8601DateFormattingDDMMYYYY(input);

        assertEquals(expectedDate.toString(), result);

    }

    @Test
    void testISO8601DateFormattingMMDDYYYY() {
        String input = "06/15/2023"; // MM/DD/YYYY
        LocalDate expectedDate = LocalDate.of(2023, 6, 15);
        String result = applicationHelper.ISO8601DateFormattingMMDDYYYY(input);

        assertEquals(expectedDate.toString(), result);

    }
}

