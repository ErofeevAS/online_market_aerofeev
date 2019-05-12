package com.gmail.erofeev.st.alexei.onlinemarket.repository.model;

public class User {
    private Long id;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private String password;
    private Role role;
    private Boolean deleted;

    public User() {
    }

    public User(String lastName, String firstName, String patronymic, String email, String password, Role role, Boolean deleted) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.password = password;
        this.role = role;
        this.deleted = deleted;
    }

    public User(String lastName, String firstName, String patronymic, String email, Role role, Boolean deleted) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
        this.email = email;
        this.role = role;
        this.deleted = deleted;
    }

    public User(long user_id, String lastName, String firstName, String patronymic) {
        this.id = user_id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.patronymic = patronymic;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", deleted=" + deleted +
                '}';
    }
}
