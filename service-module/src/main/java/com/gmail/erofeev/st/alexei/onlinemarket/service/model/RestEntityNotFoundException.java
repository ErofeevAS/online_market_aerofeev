package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.sql.SQLException;

public class RestEntityNotFoundException extends RuntimeException {

    public RestEntityNotFoundException(String message, SQLException e) {
        super(message, e);
    }

    public RestEntityNotFoundException(String message) {
        super(message);
    }

    public RestEntityNotFoundException() {

    }
}

