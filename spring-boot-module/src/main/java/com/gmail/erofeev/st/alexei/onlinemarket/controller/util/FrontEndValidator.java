package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import org.springframework.stereotype.Component;

@Component
public class FrontEndValidator {
    public Integer validateAmount(String amount) {
        int intParameter;
        try {
            intParameter = Integer.parseInt(amount);
            if (intParameter <= 0) {
                throw new NumberFormatException();
            }
            return intParameter;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
