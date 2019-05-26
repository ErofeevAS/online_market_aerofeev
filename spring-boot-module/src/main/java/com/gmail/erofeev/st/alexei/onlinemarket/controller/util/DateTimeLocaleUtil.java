package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class DateTimeLocaleUtil {
    public String getCurrentTimeInDateTimeLocaleFormat() {
        Date date = new Date();
        Timestamp currentDate = new Timestamp(date.getTime());
        int year = currentDate.toLocalDateTime().getYear();
        int month = currentDate.toLocalDateTime().getMonthValue();
        String DoubleNumberFormatMonth = getDoubleNumberFormat(month);
        int day = currentDate.toLocalDateTime().getDayOfMonth();
        String DoubleNumberFormatDay = getDoubleNumberFormat(day);
        int hour = currentDate.toLocalDateTime().getHour();
        String DoubleNumberFormatHour = getDoubleNumberFormat(hour);
        int minute = currentDate.toLocalDateTime().getMinute();
        String DoubleNumberFormatMinute = getDoubleNumberFormat(minute);
        String time = String.format("%s-%s-%sT%s:%s", year, DoubleNumberFormatMonth, DoubleNumberFormatDay, DoubleNumberFormatHour, DoubleNumberFormatMinute);
        return time;
    }

    private String getDoubleNumberFormat(int digit) {
        String doubleNumberFormat;
        if (digit < 10) {
            doubleNumberFormat = "0" + digit;
        } else {
            doubleNumberFormat = String.valueOf(digit);
        }
        return doubleNumberFormat;
    }
}
