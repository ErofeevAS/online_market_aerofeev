package com.gmail.erofeev.st.alexei.onlinemarket.service.converter.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.converter.DateTimeConverter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Component
public class DateTimeConverterImpl implements DateTimeConverter {
    @Override
    public Timestamp convertDateTimeLocaleToTimeStamp(String dateTimeLocale) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeLocale);
        return Timestamp.valueOf(localDateTime);
    }
}

