package com.gmail.erofeev.st.alexei.onlinemarket.controller.validator;

import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;

import java.util.Map;

public interface UserValidator {
    Map<String, String> validate(UserDTO user);
}
