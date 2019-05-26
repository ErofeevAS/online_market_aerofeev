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

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ABOUT_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ARTICLES_ALL_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ARTICLES_NEW_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.ITEMS_ALL_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.LOGIN_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.PROFILE_ALL_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.REDIRECT_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.REVIEWS_ALL_URL;
import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.USERS_ALL_URL;

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
                .antMatchers(USERS_ALL_URL, REVIEWS_ALL_URL)
                .hasRole(securityProperties.getRoleAdmin())
                .antMatchers(ARTICLES_NEW_URL, ITEMS_ALL_URL)
                .hasAnyRole(securityProperties.getRoleSale())
                .antMatchers(ARTICLES_ALL_URL)
                .hasAnyRole(
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())
                .antMatchers(PROFILE_ALL_URL)
                .hasAnyRole(
                        securityProperties.getRoleAdmin(),
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())
                .antMatchers(REDIRECT_URL, ABOUT_URL, LOGIN_URL)
                .permitAll()
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
