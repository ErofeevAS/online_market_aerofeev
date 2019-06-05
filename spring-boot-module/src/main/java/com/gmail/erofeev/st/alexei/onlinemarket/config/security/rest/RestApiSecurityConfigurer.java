package com.gmail.erofeev.st.alexei.onlinemarket.config.security.rest;

import com.gmail.erofeev.st.alexei.onlinemarket.config.properties.SecurityProperties;
import com.gmail.erofeev.st.alexei.onlinemarket.config.security.rest.handler.ApiAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.REST_API_ALL_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.USER_DETAILS_SERVICE_REST_API;

@Configuration
@Order(1)
public class RestApiSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final SecurityProperties securityProperties;

    @Autowired
    public RestApiSecurityConfigurer(PasswordEncoder passwordEncoder,
                                     @Qualifier(USER_DETAILS_SERVICE_REST_API) UserDetailsService userDetailsService, SecurityProperties securityProperties) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(REST_API_ALL_URL)
                .authorizeRequests()
                .anyRequest()
                .hasRole(securityProperties.getRoleSecureRestApi())
                .and()
                .httpBasic()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(apiAccessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public AccessDeniedHandler apiAccessDeniedHandler() {
        return new ApiAccessDeniedHandler();
    }
}