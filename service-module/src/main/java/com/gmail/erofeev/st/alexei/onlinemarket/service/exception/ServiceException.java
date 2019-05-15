package com.gmail.erofeev.st.alexei.onlinemarket.service.exception;

import java.sql.SQLException;

public class ServiceException extends RuntimeException {
    public ServiceException(String message, SQLException e) {
        super(message, e);
    }
}
