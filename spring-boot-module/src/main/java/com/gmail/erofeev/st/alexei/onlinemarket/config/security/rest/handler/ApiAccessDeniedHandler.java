package com.gmail.erofeev.st.alexei.onlinemarket.config.security.rest.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

public class ApiAccessDeniedHandler implements AccessDeniedHandler {
    private static Logger logger = LoggerFactory.getLogger(ApiAccessDeniedHandler.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            logger.info(String.format("%s was trying to access protected resource: %s",
                    auth.getName(),
                    request.getRequestURI()));
        }
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setStatus(HttpStatus.FORBIDDEN);
        jsonResponse.setMessage("Access denied");
        String json = objectMapper.writeValueAsString(jsonResponse);
        response.getWriter().write(json);
    }
}