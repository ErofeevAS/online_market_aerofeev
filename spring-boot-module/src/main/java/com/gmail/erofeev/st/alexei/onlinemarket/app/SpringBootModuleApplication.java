package com.gmail.erofeev.st.alexei.onlinemarket.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(
        exclude = UserDetailsServiceAutoConfiguration.class,
        scanBasePackages = {
                "com.gmail.erofeev.st.alexei.onlinemarket",
                "com.gmail.erofeev.st.alexei.onlinemarket.service",
                "com.gmail.erofeev.st.alexei.onlinemarket.repository"
        })
@EntityScan( basePackages = {"com.gmail.erofeev.st.alexei.onlinemarket.repository"} )
public class SpringBootModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }

}
