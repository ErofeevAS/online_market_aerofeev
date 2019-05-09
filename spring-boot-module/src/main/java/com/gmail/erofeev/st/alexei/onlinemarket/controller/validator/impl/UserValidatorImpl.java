package com.gmail.erofeev.st.alexei.onlinemarket.controller.validator.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.validator.UserValidator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserValidatorImpl implements UserValidator {
    private static final Integer MAX_LASTNAME_LENGTH = 40;
    private static final Integer MAX_FIRSTNAME_LENGTH = 20;
    private static final Integer MAX_PARTONYMIC_LENGTH = 40;
    private static final Integer MAX_EMAIL_LENGTH = 50;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public Map<String, String> validate(UserDTO user) {
        Map<String, String> errors = new HashMap<>();
        String lastName = user.getLastName();
        if (lastName == null) {
            errors.put("lastName", "must be not null");
        } else if (lastName.isEmpty()) {
            errors.put("lastName", "must be not empty");
        } else if (lastName.length() >= MAX_LASTNAME_LENGTH) {
            errors.put("lastName", "must be less then: " + MAX_LASTNAME_LENGTH);
        }
        String firstName = user.getFirstName();
        if (firstName == null) {
            errors.put("firstName", "must be not null");
        } else if (firstName.isEmpty()) {
            errors.put("firstName", "must be not empty");
        } else if (lastName.length() >= MAX_FIRSTNAME_LENGTH) {
            errors.put("firstName", "must be less then: " + MAX_FIRSTNAME_LENGTH);
        }
        String patronymic = user.getPatronymic();
        if (patronymic == null) {
            errors.put("patronymic", "must be not null");
        } else if (patronymic.isEmpty()) {
            errors.put("patronymic", "must be not empty");
        } else if (patronymic.length() >= MAX_PARTONYMIC_LENGTH) {
            errors.put("patronymic", "must be less then: " + MAX_PARTONYMIC_LENGTH);
        }
        String email = user.getEmail();
        if (patronymic == null) {
            errors.put("email", "must be not null");
        } else if (lastName.length() >= MAX_EMAIL_LENGTH) {
            errors.put("email", "must be less then: " + MAX_EMAIL_LENGTH);
        } else if (!validateEmail(email)) {
            errors.put("email", "email must have email pattern");
        }
        return errors;
    }

    private boolean validateEmail(String email) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
        return matcher.find();
    }
}
