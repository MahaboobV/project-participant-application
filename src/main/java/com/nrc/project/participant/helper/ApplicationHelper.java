package com.nrc.project.participant.helper;

import com.nrc.project.participant.constants.AppConstants;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ApplicationHelper {

    public String ISO8601DateFormattingDDMMYYYY(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_4);
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        DateTimeFormatter iso8601Formatter = DateTimeFormatter.ISO_DATE;
        return date.format(iso8601Formatter);
    }

    public String ISO8601DateFormattingMMDDYYYY(String dateString) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(AppConstants.DATE_FORMAT_3);
        LocalDate date = LocalDate.parse(dateString, inputFormatter);
        DateTimeFormatter iso8601Formatter = DateTimeFormatter.ISO_DATE;
        return date.format(iso8601Formatter);
    }
}
