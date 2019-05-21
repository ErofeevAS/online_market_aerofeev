package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Pattern;

@Validated
public class PasswordDTO {
    private Long id;
    @Pattern(regexp = "^[A-Za-z0-9_]{3,20}$", message = "must be from 3 to 20, only English letters and digit")
    private String oldPassword;
    @Pattern(regexp = "^[A-Za-z0-9_]{3,20}$", message = "must be from 3 to 20, only English letters and digit")
    private String newPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}