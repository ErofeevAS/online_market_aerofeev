package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.controller.validator.impl.UserValidatorImpl;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.Positive;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Validated
public class UserController {

    private final UserService userService;
    private final UserValidatorImpl userValidator;

    @Autowired
    public UserController(UserService userService, UserValidatorImpl userValidator) {
        this.userService = userService;
        this.userValidator = userValidator;
    }

    @GetMapping("/users")
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "1", required = false) @Positive int page,
                           @RequestParam(defaultValue = "10", required = false) @Positive int size) {
        Integer maxPage = userService.getAmount(size);
        Paginator paginator = new Paginator(page, maxPage, size);
        List<UserDTO> users = userService.getUsers(page, size);
        model.addAttribute("users", users);
        model.addAttribute("paginator", paginator);
        model.addAttribute("updatedUser", new UserDTO());
        return "users";
    }

    @PostMapping("/users/delete")
    public String deleteUsers(@RequestParam(value = "deletedUsersId", required = false) List<Long> usersIdForDelete) {
        userService.delete(usersIdForDelete);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/update")
    public String updateRole(@PathVariable Long id,
                             @RequestParam String roleName) {
        userService.updateRole(id, roleName);
        return "redirect:/users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        Map<String, String> errors = new HashMap<>();
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        model.addAttribute("errors", errors);
        return "adduser";
    }

    @PostMapping("/users/add")
    public String addUserPost(@ModelAttribute UserDTO user, Model model) {
        Map<String, String> errors = userValidator.validate(user);
        if (errors.isEmpty()) {
            model.addAttribute("successfulMessage", "user was added");
            userService.save(user);
            return "redirect:/users";
        } else {
            model.addAttribute("errors", errors);
            return "adduser";
        }
    }
}


