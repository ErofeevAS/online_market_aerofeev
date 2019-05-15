package com.gmail.erofeev.st.alexei.onlinemarket.config.security.application.handler;

import com.gmail.erofeev.st.alexei.onlinemarket.config.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginAccessDeniedHandler.class);
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException e) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.warn("{} was trying to access protected resources: {}",
                    authentication.getName(), request.getRequestURI());
        }
        response.sendRedirect(request.getContextPath() + securityProperties.getForbidRedirectPage());
    }
}
