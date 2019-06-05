package com.gmail.erofeev.st.alexei.onlinemarket.config.security.application.handler;

import com.gmail.erofeev.st.alexei.onlinemarket.config.properties.SecurityProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.LOGIN_URL;

@Component
public class AppAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private static final Logger logger = LoggerFactory.getLogger(AppAuthenticationSuccessHandler.class);
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        handle(httpServletRequest, httpServletResponse, authentication);
        clearAuthenticationAttributes(httpServletRequest);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String url = determinateUrl(authentication);
        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + url);
            return;
        }
        if (url.equals(LOGIN_URL)) {
            request.setAttribute("roleError", "USER WITH ROLE_SECURE_REST_API CAN'T LOGGIN");
        }
        redirectStrategy.sendRedirect(request, response, url);
    }

    private String determinateUrl(Authentication authentication) {
        boolean isAdmin = false;
        boolean isCustomer = false;
        boolean isSale = false;
        boolean isRest = false;
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String userAuthority = authority.getAuthority();
            if (userAuthority.equals(securityProperties.getRoleAdminWithPrefix())) {
                isAdmin = true;
                break;
            } else if (userAuthority.equals(securityProperties.getRoleCustomerWithPrefix())) {
                isCustomer = true;
                break;
            } else if (userAuthority.equals(securityProperties.getRoleSaleWithPrefix())) {
                isSale = true;
                break;
            } else if (userAuthority.equals(securityProperties.getRoleSecureRestApiWithPrefix())) {
                isRest = true;
                break;
            }
        }
        if (isAdmin) {
            return securityProperties.getStartAdminPage();
        } else if (isCustomer) {
            return securityProperties.getStartCustomerPage();
        } else if (isSale) {
            return securityProperties.getStartSalePage();
        } else if (isRest) {
            return LOGIN_URL;
        } else {
            logger.error("role not defined");
            throw new IllegalStateException("role not defined");
        }
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
