package com.gmail.erofeev.st.alexei.onlinemarket.repository.exception;

import java.sql.SQLException;

public class RepositoryException extends RuntimeException {
    public RepositoryException(String message, SQLException e) {
        super(message, e);
    }

    public RepositoryException(String message) {
        super(message);
    }
}
