package com.gmail.erofeev.st.alexei.onlinemarket.controller.handler;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.exception.WrongIdFormatException;
import com.gmail.erofeev.st.alexei.onlinemarket.service.exception.ServiceException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(value = {ServiceException.class, WrongIdFormatException.class, AccessDeniedException.class})
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
}
