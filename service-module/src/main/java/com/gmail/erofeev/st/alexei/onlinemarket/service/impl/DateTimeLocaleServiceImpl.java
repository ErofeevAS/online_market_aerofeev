package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.DateTimeLocaleService;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
public class DateTimeLocaleServiceImpl implements DateTimeLocaleService {
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
        return String.format("%s-%s-%sT%s:%s", year, DoubleNumberFormatMonth, DoubleNumberFormatDay, DoubleNumberFormatHour, DoubleNumberFormatMinute);
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
