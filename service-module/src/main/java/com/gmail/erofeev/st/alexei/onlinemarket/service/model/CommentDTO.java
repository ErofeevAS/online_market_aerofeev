package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.sql.Timestamp;

public class CommentDTO {
    private Long id;
    private Long articleId;
    private UserDTO user;
    private String content;
    private Timestamp date;
    private boolean isDeleted = false;
    private boolean isHidden = false;

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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", articleId=" + articleId +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", isDeleted=" + isDeleted +
                ", isHidden=" + isHidden +
                '}';
    }
}