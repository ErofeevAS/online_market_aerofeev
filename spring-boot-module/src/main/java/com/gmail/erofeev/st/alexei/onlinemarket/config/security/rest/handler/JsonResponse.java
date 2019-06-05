package com.gmail.erofeev.st.alexei.onlinemarket.config.security.rest.handler;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class JsonResponse {
    private HttpStatus status;
    private String message;
    private LocalDateTime localDateTime;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }
}
