package com.example.myapplication;

import androidx.room.TypeConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
public class Converters {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // LocalDate -> String 변환
    @TypeConverter
    public static String fromLocalDate(LocalDate date) {
        return date != null ? date.format(formatter) : null;
    }

    // String -> LocalDate 변환
    @TypeConverter
    public static LocalDate toLocalDate(String dateString) {
        return dateString != null ? LocalDate.parse(dateString, formatter) : null;
    }
}
