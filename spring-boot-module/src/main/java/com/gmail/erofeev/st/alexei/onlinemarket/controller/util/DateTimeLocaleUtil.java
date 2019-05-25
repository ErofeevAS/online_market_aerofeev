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
        String fullMonth = "";

        if (month < 10) {
            fullMonth = "0" + month;
        } else {
            fullMonth = String.valueOf(month);
        }

        int day = currentDate.toLocalDateTime().getDayOfMonth();
        String fullDay;
        if (day < 10) {
            fullDay = "0" + day;
        } else {
            fullDay = String.valueOf(day);
        }

        int hour = currentDate.toLocalDateTime().getHour();
        String fullHour;
        if (hour < 10) {
            fullHour = "0" + hour;
        } else {
            fullHour = String.valueOf(hour);
        }
        int minute = currentDate.toLocalDateTime().getMinute();
        String fullMinute;
        if (minute < 10) {
            fullMinute = "0" + minute;
        } else {
            fullMinute = String.valueOf(minute);
        }
        String time = String.format("%s-%s-%sT%s:%s", year, fullMonth, fullDay, fullHour, fullMinute);
        return time;
    }
}
