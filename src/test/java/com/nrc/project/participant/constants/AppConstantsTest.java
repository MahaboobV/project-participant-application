package com.nrc.project.participant.constants;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppConstantsTest {

    @Test
    void testAPIURL() {
        final String expectedAPIURL="https://test-api.not.nrc.no";
        final String actualAPIURL=AppConstants.API_URL;
        assertEquals(expectedAPIURL, actualAPIURL);
    }

    @Test
    void testAPIKEY() {
        final String expectedAPIKEY="NRC-API-KEY";
        final String actualAPIKEY=AppConstants.API_KEY_HEADER;
        assertEquals(expectedAPIKEY, actualAPIKEY);
    }

    @Test
    void testDATE_FORMAT_1() {
        final String expectedDF="MM/dd/yy";
        final String actualDF=AppConstants.DATE_FORMAT_1;
        assertEquals(expectedDF, actualDF);
    }

    @Test
    void testDATE_FORMAT_2() {
        final String expectedDF="MM.dd.yy";
        final String actualDF=AppConstants.DATE_FORMAT_2;
        assertEquals(expectedDF, actualDF);
    }

    @Test
    void testDATE_FORMAT_3() {
        final String expectedDF="MM/dd/yyyy";
        final String actualDF=AppConstants.DATE_FORMAT_3;
        assertEquals(expectedDF, actualDF);
    }

    @Test
    void testDATE_FORMAT_4() {
        final String expectedDF="dd/MM/yyyy";
        final String actualDF=AppConstants.DATE_FORMAT_4;
        assertEquals(expectedDF, actualDF);
    }
}
