package com.gmail.erofeev.st.alexei.onlinemarket.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.SecureRandom;

@Configuration
public class PasswordConfig {
    @Bean
    public SecureRandom getSecureRandom() {
        return new SecureRandom();
    }
}
