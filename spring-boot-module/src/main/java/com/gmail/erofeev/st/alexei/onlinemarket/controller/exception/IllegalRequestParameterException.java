package com.gmail.erofeev.st.alexei.onlinemarket.controller.exception;

public class IllegalRequestParameterException extends RuntimeException {
    public IllegalRequestParameterException(String message, NumberFormatException e) {
        super(message, e);
    }
}
