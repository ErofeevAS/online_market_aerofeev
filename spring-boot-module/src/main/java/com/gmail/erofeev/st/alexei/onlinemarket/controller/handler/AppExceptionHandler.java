package com.gmail.erofeev.st.alexei.onlinemarket.controller.handler;

import com.gmail.erofeev.st.alexei.onlinemarket.config.security.rest.handler.JsonResponse;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.IllegalRestRequestParameterException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RestEntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RuntimeException.class})
    public String defaultErrorHandler(HttpServletRequest req, Exception e) {
        req.setAttribute("exception", e.getMessage());
        req.setAttribute("url", req.getRequestURL());
        return "/error/error";
    }

    @ExceptionHandler(value = {ServiceException.class, IllegalRequestParameterException.class})
    public String expectedErrorHandler(HttpServletRequest req, Exception e) {
        req.setAttribute("exception", e.getMessage());
        req.setAttribute("url", req.getRequestURL());
        return "/error/error";
    }

    @ExceptionHandler(value = {IllegalRestRequestParameterException.class})
    public ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
        JsonResponse bodyOfResponse = getJsonResponse(ex.getMessage(), HttpStatus.CONFLICT);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {RestEntityNotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(RuntimeException ex, WebRequest request) {
        JsonResponse bodyOfResponse = getJsonResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        JsonResponse apiError = new JsonResponse();
        apiError.setMessage(ex.getMessage());
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        JsonResponse apiError = new JsonResponse();
        apiError.setMessage("wrong json format");
        apiError.setErrors(errors);
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        JsonResponse apiError = new JsonResponse();
        apiError.setMessage(HttpStatus.METHOD_NOT_ALLOWED.toString());
        apiError.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    private JsonResponse getJsonResponse(String message, HttpStatus status) {
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStatus(status);
        jsonResponse.setMessage(message);
        return jsonResponse;
    }
}