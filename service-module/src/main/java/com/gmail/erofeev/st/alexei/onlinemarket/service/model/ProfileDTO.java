package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

public class ProfileDTO {
    private Long id;
    private String address = "";
    private String phone = "";

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
}