package com.gmail.erofeev.st.alexei.onlinemarket.service.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.service.PasswordService;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordServiceImpl implements PasswordService {
    private final PasswordGenerator passwordGenerator;

    @Autowired
    public PasswordServiceImpl(PasswordGenerator passwordGenerator) {
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public String generatePassword(int length) {
        CharacterRule rule = new CharacterRule(EnglishCharacterData.Alphabetical);
        return passwordGenerator.generatePassword(length, rule);
    }

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }
}
