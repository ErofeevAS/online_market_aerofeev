package com.gmail.erofeev.st.alexei.onlinemarket.service.converter;

import java.sql.Timestamp;

public interface DateTimeConverter {
    Timestamp convertDateTimeLocaleToTimeStamp(String dateTimeLocale);
}
