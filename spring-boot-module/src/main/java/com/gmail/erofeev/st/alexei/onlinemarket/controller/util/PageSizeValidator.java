package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import org.springframework.stereotype.Component;

@Component
public class PageSizeValidator {
    private int intSize;
    private int intPage;

    public int validatePage(String page) {
        try {
            intPage = Integer.parseInt(page);
        } catch (NumberFormatException e) {
            intPage = 1;
        }
        if (intPage < 1) {
            intPage = 1;
        }
        return intPage;
    }

    public int validateSize(String size) {
        try {
            intSize = Integer.parseInt(size);
        } catch (NumberFormatException e) {
            intSize = 10;
        }
        if (intSize > 100 || intSize < 1) {
            intSize = 10;
        }
        return intSize;
    }
}
