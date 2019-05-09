package com.gmail.erofeev.st.alexei.onlinemarket.repository.model;

import java.sql.Timestamp;

public class Comment {
    private Long id;
    private User user;
    private Timestamp date;
    private String content;

    public Comment() {
    }

    public Comment(User user, Timestamp date, String content) {
        this.user = user;
        this.date = date;
        this.content = content;
    }

    public Comment(Long id, User user, Timestamp date, String content) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
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
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", date=" + date +
                ", content='" + content + '\'' +
                '}';
    }
}
