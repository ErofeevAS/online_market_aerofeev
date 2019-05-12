package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final Paginator paginator;

    @Autowired
    public UserController(UserService userService, Paginator paginator) {
        this.userService = userService;
        this.paginator = paginator;
    }

    @GetMapping("/users")
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "1", required = false) int page,
                           @RequestParam(defaultValue = "10", required = false) int size) {
        Integer maxPage = userService.getAmount(size);
        paginator.validate(page, maxPage, size);
        List<UserDTO> users = userService.getUsers(paginator.getPage(), paginator.getSize());
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
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        return "adduser";
    }

    @PostMapping("/users/add")
    public String addUserPost(@ModelAttribute("user") @Valid UserDTO user, BindingResult
            bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        UserDTO registeredUser = userService.register(user);
        if (registeredUser != null) {
            model.addAttribute("info", "user was registered");
            return "adduser";
        } else {
            model.addAttribute("info", "user with the same email exist");
            return "adduser";
        }
    }

    @PostMapping("/users/changepassword")
    public String changePassword(@RequestParam Long idForPasswordChange) {
        UserDTO userById = userService.findUserById(idForPasswordChange);
        userService.changePassword(userById);
        return "redirect:/users";
    }
}


