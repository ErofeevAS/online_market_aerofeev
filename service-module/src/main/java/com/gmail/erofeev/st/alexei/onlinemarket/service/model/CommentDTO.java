package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Validated
public class CommentDTO {
    private Long id;
    private Long articleId;
    private UserDTO user;
    @Size(min = 1, max = 200)
    private String content;
    private Timestamp createdDate;

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
                ", createdDate=" + createdDate +
                '}';
    }
}