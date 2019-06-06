package com.gmail.erofeev.st.alexei.onlinemarket.controller;

import com.gmail.erofeev.st.alexei.onlinemarket.controller.util.Paginator;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserAuthenticationService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.UserService;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PageDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.PasswordDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.ProfileViewDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.RoleDTO;
import com.gmail.erofeev.st.alexei.onlinemarket.service.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public UserController(UserService userService, UserAuthenticationService userAuthenticationService) {
        this.userService = userService;
        this.userAuthenticationService = userAuthenticationService;
    }

    @GetMapping("/users")
    public String getUsers(Model model,
                           @RequestParam(defaultValue = "1", required = false) String page,
                           @RequestParam(defaultValue = "10", required = false) String size) {
        Paginator paginator = new Paginator(page, size);
        PageDTO<UserDTO> pageDTO = userService.getUsers(paginator.getPage(), paginator.getSize(), false);
        paginator.setMaxPage(pageDTO.getAmountOfPages());
        model.addAttribute("users", pageDTO.getList());
        model.addAttribute("paginator", paginator);
        model.addAttribute("updatedUser", new UserDTO());
        List<RoleDTO> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        return "users";
    }

    @PostMapping("/users/delete")
    public String deleteUsers(@RequestParam(value = "deletedUsersId", required = false) List<Long> usersIdForDelete) {
        userService.delete(usersIdForDelete);
        return "redirect:/users";
    }

    @PostMapping("/users/{id}/update")
    public String updateRole(@PathVariable Long id,
                             @RequestParam Long roleId) {
        userService.updateRole(id, roleId);
        return "redirect:/users";
    }

    @GetMapping("/users/add")
    public String addUser(Model model) {
        UserDTO user = new UserDTO();
        model.addAttribute("user", user);
        List<RoleDTO> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        return "adduser";
    }

    @PostMapping("/users/add")
    public String addUserPost(@ModelAttribute("user") @Valid UserDTO user,
                              BindingResult bindingResult,
                              Model model) {
        List<RoleDTO> roles = userService.getAllRoles();
        model.addAttribute("roles", roles);
        if (bindingResult.hasErrors()) {
            return "adduser";
        }
        UserDTO registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
            model.addAttribute("info", "user was registered");
            return "adduser";
        } else {
            model.addAttribute("info", "user with the same email exist");
            return "adduser";
        }
    }

    @GetMapping("/profile")
    public String getProfile(Model model, Authentication authentication) {
        Long id = userAuthenticationService.getSecureUserId(authentication);
        ProfileViewDTO profileView = userService.getProfileView(id);
        model.addAttribute("profileView", profileView);
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setId(id);
        model.addAttribute("passwordDTO", passwordDTO);
        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@ModelAttribute("profileView") @Valid ProfileViewDTO profileView,
                                BindingResult bindingResult,
                                Model model,
                                Authentication authentication) {
        PasswordDTO passwordDTO = new PasswordDTO();
        model.addAttribute("passwordDTO", passwordDTO);
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        Long id = userAuthenticationService.getSecureUserId(authentication);
        userService.updateProfile(id, profileView);
        model.addAttribute("info", "profile was update");
        return "profile";
    }

    @PostMapping("/profile/changepassword")
    public String changePasswordForUser(@ModelAttribute("passwordDTO") @Valid PasswordDTO passwordDTO,
                                        BindingResult bindingResult,
                                        Model model,
                                        Authentication authentication) {
        Long id = userAuthenticationService.getSecureUserId(authentication);
        ProfileViewDTO profileView = userService.getProfileView(id);
        model.addAttribute("profileView", profileView);
        if (!userService.changeOldPassword(id, passwordDTO)) {
            model.addAttribute("infoPassword", "password wrong");
            return "profile";
        }
        model.addAttribute("passwordDTO", passwordDTO);
        if (bindingResult.hasErrors()) {
            return "profile";
        }
        model.addAttribute("infoPassword", "password was changed");
        return "profile";
    }

    @PostMapping("/users/changepassword")
    public String changePasswordForRandomPassword(@RequestParam(name = "userId") Long id) {
        userService.changePassword(id);
        return "redirect:/users";
    }
}
