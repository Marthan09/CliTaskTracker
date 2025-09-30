package com.marthan.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateFormarter {
    public static String formatterLocalDateTime(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy:MM:dd - hh:mm:ss"));
    }
}
