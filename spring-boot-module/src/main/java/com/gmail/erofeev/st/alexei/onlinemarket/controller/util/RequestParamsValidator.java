package com.gmail.erofeev.st.alexei.onlinemarket.controller.util;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRestRequestParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RequestParamsValidator {
    private static final Logger logger = LoggerFactory.getLogger(RequestParamsValidator.class);
    private static final String ERROR_MESSAGE = "wrong parameter. Can not parse %s to number ";

    public int validateInt(String param) {
        try {
            return defaultValidateInt(param);
        } catch (NumberFormatException e) {
            String message = String.format(ERROR_MESSAGE, param);
            logger.error(message, e);
            throw new IllegalRequestParameterException(message + e.getMessage(), e);
        }
    }

    public Long validateLong(String param) {
        try {
            return defaultValidateLong(param);
        } catch (NumberFormatException e) {
            String message = String.format(ERROR_MESSAGE, param);
            logger.error(message, e);
            throw new IllegalRequestParameterException(message + e.getMessage(), e);
        }
    }

    public int validateIntRest(String param) {
        try {
            return defaultValidateInt(param);
        } catch (NumberFormatException e) {
            String message = String.format(ERROR_MESSAGE, param);
            logger.error(message, e);
            throw new IllegalRestRequestParameterException(message + e.getMessage(), e);
        }
    }

    public Long validateLongRest(String param) {
        try {
            return defaultValidateLong(param);
        } catch (NumberFormatException e) {
            String message = String.format(ERROR_MESSAGE, param);
            logger.error(message, e);
            throw new IllegalRestRequestParameterException(message + e.getMessage(), e);
        }
    }

    private Long defaultValidateLong(String param) throws NumberFormatException {
        Long longParameter;
        longParameter = Long.parseLong(param);
        if (longParameter < 0) {
            throw new NumberFormatException();
        }
        return longParameter;
    }

    private Integer defaultValidateInt(String param) throws NumberFormatException {
        int intParameter;
        intParameter = Integer.parseInt(param);
        if (intParameter < 0) {
            throw new NumberFormatException();
        }
        return intParameter;
    }

}
