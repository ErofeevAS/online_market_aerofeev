package com.gmail.erofeev.st.alexei.onlinemarket.controller.handler;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRestRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {
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
    protected ResponseEntity<Object> handleConflict(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {RestEntityNotFoundException.class})
    protected ResponseEntity<Object> handleNotFoundException(
            RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}
