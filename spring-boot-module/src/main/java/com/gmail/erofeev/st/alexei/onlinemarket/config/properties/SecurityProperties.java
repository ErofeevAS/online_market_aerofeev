package com.gmail.erofeev.st.alexei.onlinemarket.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityProperties {

    @Value("${app.security.role.customer}")
    private String roleCustomer;
    @Value("${app.security.role.admin}")
    private String roleAdmin;
    @Value("${app.security.role.securerestapi}")
    private String roleSecureRestApi;
    @Value("${app.security.bcrypt.strength}")
    private String bcryptRounds;
    @Value("${app.security.forbid.redirect.page}")
    private String forbidRedirectPage;
    @Value("${app.security.start.admin.page}")
    private String startAdminPage;
    @Value("${app.security.start.customer.page}")
    private String startCustomerPage;

    public String getRoleCustomer() {
        return  roleCustomer;
    }

    public String getRoleAdmin() {
        return  roleAdmin;
    }

    public int getBcryptRounds() {
        return Integer.parseInt(bcryptRounds);
    }

    public String getForbidRedirectPage() {
        return forbidRedirectPage;
    }

    public String getRoleSecureRestApi() {
        return roleSecureRestApi;
    }

    public String getStartAdminPage() {
        return startAdminPage;
    }

    public String getStartCustomerPage() {
        return startCustomerPage;
    }
}


