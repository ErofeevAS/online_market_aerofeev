package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.sql.Timestamp;
import java.util.List;

public class ArticleDTO {
    private Long id;
    private String title;
    private UserDTO user;
    private String content;
    private Timestamp date;
    private Boolean deleted;
    private Boolean hided;
    private List<CommentDTO> comments;

    public ArticleDTO() {
    }

    public ArticleDTO(Long id, UserDTO user, String content, Timestamp date, Boolean deleted, Boolean hided) {
        this.id = id;
        this.user = user;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
        this.hided = hided;
    }

    public ArticleDTO(String content, Timestamp date, Boolean deleted, Boolean hided) {
        this.content = content;
        this.date = date;
        this.deleted = deleted;
        this.hided = hided;
    }

    public ArticleDTO(Long id, String title, UserDTO user, String content, Timestamp date, Boolean deleted, Boolean hided, List<CommentDTO> comments) {
        this.id = id;
        this.title = title;
        this.user = user;
        this.content = content;
        this.date = date;
        this.deleted = deleted;
        this.hided = hided;
        this.comments = comments;
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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", deleted=" + deleted +
                ", hided=" + hided +
                ", comments=" + comments +
                '}';
    }
}
