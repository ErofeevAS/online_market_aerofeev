package com.gmail.erofeev.st.alexei.onlinemarket.controller.exception;

public class WrongRequestParameterException extends RuntimeException {
    public WrongRequestParameterException(String message, NumberFormatException e) {
        super(message, e);
    }
}
