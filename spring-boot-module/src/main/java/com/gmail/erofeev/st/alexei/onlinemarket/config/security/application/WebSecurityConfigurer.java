package com.gmail.erofeev.st.alexei.onlinemarket.config.security.application;

import com.gmail.erofeev.st.alexei.onlinemarket.config.properties.SecurityProperties;
import com.gmail.erofeev.st.alexei.onlinemarket.config.security.application.handler.AppAuthenticationSuccessHandler;
import com.gmail.erofeev.st.alexei.onlinemarket.config.security.application.handler.LoginAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.*;

@Configuration
@Order(2)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    @Autowired
    public WebSecurityConfigurer(UserDetailsService userDetailsService,
                                 PasswordEncoder passwordEncoder,
                                 SecurityProperties securityProperties) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.securityProperties = securityProperties;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()

                .antMatchers(REDIRECT_URL, ABOUT_URL, LOGIN_URL)
                .permitAll()
                .antMatchers(
                        "/orders/*",
                        "/articles", "/articles/tag/*", "/articles/*/newComment",
                        "/items", "/items/*")
                .hasAnyRole(
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())

                .antMatchers(ARTICLES_NEW_URL, ITEMS_ALL_URL, "/articles/*/update",
                        "/articles/*/deleteComment",
                        "/orders",
                        "/orders/*/update")
                .hasRole(securityProperties.getRoleSale())

                .antMatchers(
                        "/orders/sale/new",
                        "/userorders",
                        "/users/*/orders",
                        "/reviews/new",
                        "wrongAmount.html")
                .hasRole(securityProperties.getRoleCustomer())

                .antMatchers(USERS_ALL_URL, "/reviews","/reviews/*/delete","/reviews/update")
                .hasRole(securityProperties.getRoleAdmin())

                .antMatchers(PROFILE_ALL_URL)
                .hasAnyRole(
                        securityProperties.getRoleAdmin(),
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())

                .and()
                .formLogin()
                .loginPage(LOGIN_URL)

                .successHandler(authenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AppAuthenticationSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new LoginAccessDeniedHandler();
    }
}
