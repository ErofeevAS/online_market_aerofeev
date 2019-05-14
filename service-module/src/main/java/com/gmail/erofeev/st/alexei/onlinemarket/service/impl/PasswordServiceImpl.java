package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final SecureRandom secureRandom;
    @Value("${app.generated.password.pattern.upper}")
    private String charactersPatter;
    @Value("${app.generated.password.pattern.digits}")
    private String digitsPatter;

    @Autowired
    public PasswordServiceImpl(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    public String generatePassword(int length) {
        char[] pattern = (charactersPatter + digitsPatter).toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            char randomChar = pattern[secureRandom.nextInt(pattern.length + 1)];
            buffer.append(randomChar);
        }
        return buffer.toString();
    }
}
