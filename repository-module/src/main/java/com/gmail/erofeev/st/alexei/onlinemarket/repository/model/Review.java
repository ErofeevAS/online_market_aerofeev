package com.gmail.erofeev.st.alexei.onlinemarket.repository.model;

import java.sql.Timestamp;

public class Review {
    private Long id;
    private String title;
    private User user;
    private String content;
    private Timestamp date;
    private Boolean deleted;
    private Boolean hided;

    public Review() {
    }

    public Review(Long id, String title, User user, String content, Timestamp date, Boolean deleted, Boolean hided) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
        this.hided = hided;
    }

    public Review(User user, String content, Timestamp date, Boolean deleted, Boolean hided) {
        this.user = user;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
        this.hided = hided;
    }

    public Review(Long id, Boolean deleted, Boolean hided) {
        this.id = id;
        this.deleted = deleted;
        this.hided = hided;
    }

    public Review(Long id, User user, String content, Timestamp date, boolean deleted, boolean hided) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
        this.hided = hided;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getHided() {
        return hided;
    }

    public void setHided(Boolean hided) {
        this.hided = hided;
    }


    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", deleted=" + deleted +
                ", hided=" + hided +
                '}';
    }
}


