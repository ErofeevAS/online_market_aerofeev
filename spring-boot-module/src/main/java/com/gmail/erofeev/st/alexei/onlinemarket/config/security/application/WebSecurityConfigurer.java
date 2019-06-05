package com.gmail.erofeev.st.alexei.onlinemarket.config.security.application;

import com.gmail.erofeev.st.alexei.onlinemarket.config.properties.SecurityProperties;
import com.gmail.erofeev.st.alexei.onlinemarket.config.security.application.handler.AppAuthenticationSuccessHandler;
import com.gmail.erofeev.st.alexei.onlinemarket.config.security.application.handler.LoginAccessDeniedHandler;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import static com.gmail.erofeev.st.alexei.onlinemarket.config.properties.GlobalConstants.*;

@Configuration
@Order(2)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityProperties securityProperties;

    @Autowired
    public WebSecurityConfigurer(@Qualifier(USER_DETAILS_SERVICE) UserDetailsService userDetailsService,
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
                .antMatchers(REDIRECT_URL, LOGIN_URL, ERROR_URL)
                .permitAll()
                .antMatchers(
                        ORDER_GET_BY_ID_URL,
                        ARTICLES_GET_ALL_URL,
                        ARTICLES_GET_BY_ID_URL,
                        ARTICLES_SELECT_BY_TAG_URL,
                        ARTICLES_NEW_COMMENT_URL,
                        ITEMS_GET_ALL_URL,
                        ITEMS_GET_BY_ID_URL)
                .hasAnyRole(
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())
                .antMatchers(ITEMS_ALL_URL,
                        ARTICLES_NEW_URL,
                        ARTICLES_UPDATE_URL,
                        ARTICLES_DELETE_URL,
                        ARTICLES_DELETE_COMMENT_URL,
                        UPLOAD_FILE_URL,
                        ORDERS_GET_ALL_URL,
                        ORDERS_UPDATE_URL)
                .hasRole(securityProperties.getRoleSale())
                .antMatchers(
                        ORDERS_NEW_URL,
                        ORDERS_FOR_USER_URL,
                        USER_ORDERS,
                        REVIEWS_NEW_URL,
                        WRONG_AMOUNT_PAGE)
                .hasRole(securityProperties.getRoleCustomer())

                .antMatchers(USERS_ALL_URL,
                        REVIEWS_GET_ALL_URL,
                        REVIEWS_DELETE_URL,
                        REVIEWS_UPDATE_URL)
                .hasRole(securityProperties.getRoleAdmin())

                .antMatchers(PROFILE_ALL_URL)
                .hasAnyRole(
                        securityProperties.getRoleAdmin(),
                        securityProperties.getRoleCustomer(),
                        securityProperties.getRoleSale())

                .antMatchers("/**")
                .denyAll()
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
