package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.PageSizeValidator;
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
    private final PageSizeValidator pageSizeValidator;

    @Autowired
    public UserController(UserService userService, Paginator paginator, PageSizeValidator pageSizeValidator) {
        this.userService = userService;
        this.paginator = paginator;
        this.pageSizeValidator = pageSizeValidator;
    }

    @GetMapping("/users")
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "1", required = false) String page,
                           @RequestParam(defaultValue = "10", required = false) String size) {
        int intPage = pageSizeValidator.validatePage(page);
        int intSize = pageSizeValidator.validateSize(size);
        Integer maxPage = userService.getAmount(intSize);
        paginator.validate(intPage, maxPage, intSize);
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
    public String changePassword(@RequestParam Long userId) {
        UserDTO userById = userService.findUserById(userId);
        userService.changePassword(userById);
        return "redirect:/users";
    }
}


