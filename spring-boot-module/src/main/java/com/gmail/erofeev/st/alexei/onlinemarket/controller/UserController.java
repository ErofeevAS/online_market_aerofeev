package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.AppUserPrincipal;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "1", required = false) String page,
                           @RequestParam(defaultValue = "10", required = false) String size) {
        Paginator paginator = new Paginator(page, size);
        PageDTO<UserDTO> pageDTO = userService.findAll(paginator.getPage(), paginator.getSize());
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("users", pageDTO.getList());
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

    @GetMapping("/users/{id}/profile")
    public String getProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        Long id = principal.getUser().getId();
        ProfileViewDTO profileView = userService.getProfileView(id);
        model.addAttribute("profileView", profileView);
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setId(id);
        model.addAttribute("passwordDTO", passwordDTO);
        return "profile";
    }

    @PostMapping("/users/{id}/profile")
    public String updateProfile(@PathVariable Long id,
                                @ModelAttribute("profileView") @Valid ProfileViewDTO profileView,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        userService.updateProfile(id, profileView);
        model.addAttribute("info", "profile was update");
        return "profile";
    }

    @PostMapping("/users/{id}/changepassword")
    public String changePasswordForUser(@PathVariable Long id,
                                        @ModelAttribute("passwordDTO") @Valid PasswordDTO passwordDTO) {
        userService.changeOldPassword(id, passwordDTO);
        return "redirect:/users";
    }
}


