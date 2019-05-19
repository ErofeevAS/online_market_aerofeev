package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
public class UserDTO {
    private Long id;
    @NotNull
    @Pattern(regexp = "^[A-Za-z_]{1,40}$", message = "must be from 1 to 40, only English letters")
    private String lastName;
    @NotNull
    @Pattern(regexp = "^[A-Za-z_]{1,20}$", message = "must be from 1 to 20, only English letters")
    private String firstName;
    @Pattern(regexp = "^[A-Za-z_]{0,40}$", message = "must be from 1 to 40, only English letters")
    private String patronymic = "";
    private String password;
    private String oldPassword;
    @NotNull
    @NotEmpty
    @Email
    private String email;
    private RoleDTO role;
    private Boolean deleted = false;
    private ProfileDTO profile;

    public UserDTO() {
    }

    public UserDTO(Long id, String lastName, String firstName, String patronymic, String email, RoleDTO role, Boolean deleted) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.role = role;
        this.deleted = deleted;
    }

    public String getFullName() {
        return String.format("%s %s %s", lastName, firstName, patronymic);
    }

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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProfileDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileDTO profile) {
        this.profile = profile;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", deleted=" + deleted +
                '}';
    }
}
