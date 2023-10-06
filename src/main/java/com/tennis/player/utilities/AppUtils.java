package com.tennis.player.utilities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppUtils {

    public static final String DATE_PATTERN_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String formatTimestamp(LocalDateTime localDateTime, String patternFormat) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(patternFormat);
	return formatter.format(localDateTime);
    }

    public static String formatTimestamp(LocalDateTime localDateTime) {
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN_FORMAT);
	return formatter.format(localDateTime);
    }
}
