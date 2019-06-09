package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.WrongIdFormatException;
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

    public Long validateId(String id) {
        Long validatedId;
        try {
            validatedId = Long.parseLong(id);
            if (validatedId <= 0) {
                throw new NumberFormatException();
            }
            return validatedId;
        } catch (NumberFormatException e) {
            throw new WrongIdFormatException(id + " must be Long type");
        }
    }

    public boolean isTextShorterThan(String text, int length) {
        return text.length() < length && text.length() > 0;
    }
}
