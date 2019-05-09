package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.sql.Timestamp;

public class CommentDTO {
    private Long id;
    private UserDTO user;
    private Timestamp date;
    private String content;

    public CommentDTO() {
    }

    public CommentDTO(Long id, UserDTO user, Timestamp date, String content) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.content = content;
    }

    public CommentDTO(UserDTO user, Timestamp date, String content) {
        this.user = user;
        this.date = date;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}
