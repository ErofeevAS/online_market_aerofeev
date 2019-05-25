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
                .antMatchers("/adduser", "/users/**", "/reviews/**")
                .hasRole(securityProperties.getRoleAdmin())

                .antMatchers("/articles/new")
                .hasAnyRole(securityProperties.getRoleSale())

                .antMatchers("/articles/**")
                .hasAnyRole(
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())

                .antMatchers("/profile/**")
                .hasAnyRole(
                        securityProperties.getRoleAdmin(),
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())
                .antMatchers("/403", "/about", "/login")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
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
