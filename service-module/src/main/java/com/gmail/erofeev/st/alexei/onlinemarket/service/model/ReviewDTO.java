package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.sql.Timestamp;

public class ReviewDTO {
    private Long id;
    private UserDTO user;
    private String content;
    private Timestamp createdDate;
    private Boolean deleted = false;
    private Boolean hidden = false;

    public ReviewDTO() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", deleted=" + deleted +
                ", hidden=" + hidden +
                '}';
    }
}