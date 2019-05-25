package com.gmail.erofeev.st.alexei.onlinemarket.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityProperties {
    @Value("${app.security.role.prefix}")
    private String prefix;
    @Value("${app.security.role.customer}")
    private String roleCustomer;
    @Value("${app.security.role.admin}")
    private String roleAdmin;
    @Value("${app.security.role.securerestapi}")
    private String roleSecureRestApi;
    @Value("${app.security.role.sale}")
    private String roleSale;
    @Value("${app.security.bcrypt.strength}")
    private String bcryptRounds;
    @Value("${app.security.forbid.redirect.page}")
    private String forbidRedirectPage;
    @Value("${app.security.start.admin.page}")
    private String startAdminPage;
    @Value("${app.security.start.customer.page}")
    private String startCustomerPage;
    @Value("${app.security.start.sale.page}")
    private String startSalePage;

    public String getRoleCustomerWithPrefix() {
        return prefix + roleCustomer;
    }

    public String getRoleAdminWithPrefix() {
        return prefix + roleAdmin;
    }

    public String getRoleSecureRestApiWithPrefix() {
        return prefix + roleSecureRestApi;
    }

    public String getRoleSaleWithPrefix() {
        return prefix + roleSale;
    }

    public int getBcryptRounds() {
        return Integer.parseInt(bcryptRounds);
    }

    public String getForbidRedirectPage() {
        return forbidRedirectPage;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getRoleCustomer() {
        return roleCustomer;
    }

    public String getRoleAdmin() {
        return roleAdmin;
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

    public String getRoleSale() {
        return roleSale;
    }

    public String getStartSalePage() {
        return startSalePage;
    }
}


