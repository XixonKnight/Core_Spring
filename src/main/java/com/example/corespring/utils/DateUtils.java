package com.example.corespring.utils;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@UtilityClass
@Slf4j
public class DateUtils {

    private final List<String> SUPPORTED_FORMATS = List.of(
            "yyyy-MM-dd", "dd/MM/yyyy",
            "MM-dd-yyyy", "dd-MM-yyyy"
    );

    private final List<String> LOCAL_DATETIME_FORMAT = List.of(
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss",
            "dd/MM/yyyy HH:mm:ss",
            "dd-MM-yyyy HH:mm:ss",
            "yyyy/MM/dd HH:mm:ss"
    );


    /**
     * convert string to localDate
     * @param dateStr String date
     * @param pattern pattern
     * @return LocalDate
     */
    public static LocalDate stringToLocalDate(String dateStr, String pattern) {
        if (dateStr == null || pattern == null) {
            log.error("Invalid date format: dateStr: {}, pattern: {}", dateStr, pattern);
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            return LocalDate.parse(dateStr, formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: dateStr: {}, pattern: {}, error:{}", dateStr, pattern, e.getMessage());
            return null;
        }
    }

    /**
     * Chuyển String sang LocalDateTime theo định dạng truyền vào.
     * @param dateTimeStr chuỗi ngày giờ
     * @param pattern định dạng ngày giờ, ví dụ "yyyy-MM-dd HH:mm:ss"
     * @return LocalDateTime hoặc null nếu không parse được
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeStr, String pattern) {
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDateTime.parse(dateTimeStr, formatter);
        } catch (DateTimeParseException e) {
            log.error("Invalid date format: dateTimeStr: {}, pattern: {}, error:{}", dateTimeStr, pattern, e.getMessage());
            return null;
        }
    }


    public static LocalDate stringToLocalDate(String value) {
        LocalDate date = null;
        for (String formatter : SUPPORTED_FORMATS) {
            date = stringToLocalDate(value, formatter);
            if (date != null) {
                break;
            }
        }
        return date;
    }

    public static LocalDateTime stringToLocalDateTime(String value) {
        LocalDateTime date = null;
        for (String formatter : LOCAL_DATETIME_FORMAT) {
            date = stringToLocalDateTime(value, formatter);
            if (date != null) {
                break;
            }
        }
        return date;
    }

    public static void main(String[] args) {
        String dateStr = "2025/02/20 10:25:11";
        String date = "2025-05-12";
        log.info("localDateTime: {}", stringToLocalDateTime(dateStr));
        log.info("localDate: {}", stringToLocalDate(date));
    }


}
