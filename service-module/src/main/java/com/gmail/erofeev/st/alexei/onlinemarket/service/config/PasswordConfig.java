package com.gmail.erofeev.st.alexei.onlinemarket.service.config;

import org.passay.PasswordGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordConfig {

    @Bean
    PasswordGenerator generate() {
        PasswordGenerator passwordGenerator = new PasswordGenerator();
        return passwordGenerator;
    }
}
