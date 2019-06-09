package com.gmail.erofeev.st.alexei.onlinemarket.controller.exception;

public class IllegalRestRequestParameterException extends RuntimeException {
    public IllegalRestRequestParameterException(String message, NumberFormatException e) {
        super(message, e);
    }
}
