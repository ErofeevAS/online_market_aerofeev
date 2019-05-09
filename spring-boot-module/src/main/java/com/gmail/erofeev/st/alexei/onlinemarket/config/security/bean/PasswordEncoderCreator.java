package com.gmail.erofeev.st.alexei.onlinemarket.config.security.bean;

import com.gmail.erofeev.st.alexei.onlinemarket.config.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderCreator {

    @Autowired
    private final SecurityProperties securityProperties;

    public PasswordEncoderCreator(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(securityProperties.getBcryptRounds());
    }
}
