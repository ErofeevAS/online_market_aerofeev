package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.WrongRequestParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestParamsValidator {
    private static final Logger logger = LoggerFactory.getLogger(RequestParamsValidator.class);

    public int validateInt(String param) {
        int intParameter;
        try {
            intParameter = Integer.parseInt(param);
            if (intParameter < 0) {
                throw new NumberFormatException();
            }
            return intParameter;
        } catch (NumberFormatException e) {
            logger.error(String.format("wrong parameter. Can not parse %s to number ", param), e);
            throw new WrongRequestParameterException(e.getMessage(), e);
        }
    }

    public Long validateLong(String param) {
        Long longParameter;
        try {
            longParameter = Long.parseLong(param);
            if (longParameter < 0) {
                throw new NumberFormatException();
            }
            return longParameter;
        } catch (NumberFormatException e) {
            logger.error(String.format("wrong parameter. Can not parse %s to number ", param), e);
            throw new WrongRequestParameterException(e.getMessage(), e);
        }
    }
}
