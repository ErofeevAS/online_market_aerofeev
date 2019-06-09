package com.gmail.erofeev.st.alexei.onlinemarket.controller.exception;

public class WrongIdFormatException extends RuntimeException {
    public WrongIdFormatException(String message, Exception e) {
        super(message, e);
    }

    public WrongIdFormatException(String message) {
        super(message);
    }
}
