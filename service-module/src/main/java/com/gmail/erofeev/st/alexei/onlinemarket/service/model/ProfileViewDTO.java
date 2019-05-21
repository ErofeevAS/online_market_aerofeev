package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import javax.validation.constraints.Pattern;

public class ProfileViewDTO {
    private Long id;
    @Pattern(regexp = "^[A-Za-z_]{0,40}$", message = "must be from 1 to 40, only English letters")
    private String firstName;
    @Pattern(regexp = "^[A-Za-z_]{0,40}$", message = "must be from 1 to 40, only English letters")
    private String lastName;
    @Pattern(regexp = "^[A-Za-z_]{0,40}$", message = "must be from 1 to 40, only English letters")
    private String address;
    @Pattern(regexp = "^[0-9_]{5,12}$", message = "must be from 5 to 12, only digit letters")
    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}