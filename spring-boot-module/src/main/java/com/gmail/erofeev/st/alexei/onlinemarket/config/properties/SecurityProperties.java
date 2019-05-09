package com.gmail.erofeev.st.alexei.onlinemarket.config.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecurityProperties {

    @Value("${app.security.role.user}")
    private String roleUser;
    @Value("${app.security.role.admin}")
    private String roleAdmin;
    @Value("${app.security.role.prefix}")
    private String rolePrefix;
    @Value("${app.security.bcrypt.strength}")
    private String bcryptRounds;
    @Value("${app.security.forbid.redirect.page}")
    private String forbidRedirectPage;

    public String getRoleUser() {
        return rolePrefix + roleUser;
    }

    public String getRoleAdmin() {
        return rolePrefix + roleAdmin;
    }

    public int getBcryptRounds() {
        return Integer.parseInt(bcryptRounds);
    }

    public String getForbidRedirectPage() {
        return forbidRedirectPage;
    }
}


