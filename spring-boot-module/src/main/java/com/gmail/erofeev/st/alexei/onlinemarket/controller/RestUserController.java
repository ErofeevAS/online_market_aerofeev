package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserRestDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    private final UserService userService;

    public RestUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserRestDTO saveUser(@Valid @RequestBody UserRestDTO userDTO) {
        return userService.registerFromRest(userDTO);
    }
}
