package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
public class UserRestDTO {
    private Long id;
    @NotNull
    @Pattern(regexp = "^[A-Za-z_\\s]{1,40}$", message = "must be from 1 to 40, only English letters")
    private String lastName;
    @NotNull
    @Pattern(regexp = "^[A-Za-z_\\s]{1,20}$", message = "must be from 1 to 20, only English letters")
    private String firstName;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    @NotNull
    private Long roleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
