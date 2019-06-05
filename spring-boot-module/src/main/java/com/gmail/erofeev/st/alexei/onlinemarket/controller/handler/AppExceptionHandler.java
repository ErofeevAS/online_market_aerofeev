package com.gmail.erofeev.st.alexei.onlinemarket.controller.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.erofeev.st.alexei.onlinemarket.config.security.rest.handler.JsonResponse;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRestRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
    private final ObjectMapper objectMapper;

    @Autowired
    public AppExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @ExceptionHandler(value = {ServiceException.class, IllegalRequestParameterException.class})
    public String expectedErrorHandler(HttpServletRequest req, Exception e) {
        req.setAttribute("exception", e.getMessage());
        req.setAttribute("url", req.getRequestURL());
        return "/error/error";
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public String defaultErrorHandler(HttpServletRequest req, Exception e) {
        req.setAttribute("exception", e.getMessage());
        req.setAttribute("url", req.getRequestURL());
        return "/error/error";
    }

    @ExceptionHandler(value = {IllegalRestRequestParameterException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) throws JsonProcessingException {
        String bodyOfResponse = getJsonResponse(ex.getMessage(), HttpStatus.CONFLICT);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {RestEntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) throws JsonProcessingException {
        String bodyOfResponse = getJsonResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    private String getJsonResponse(String message, HttpStatus status) throws JsonProcessingException {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setLocalDateTime(LocalDateTime.now());
        jsonResponse.setStatus(status);
        jsonResponse.setMessage(message);
        return objectMapper.writeValueAsString(jsonResponse);
    }
}