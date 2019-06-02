package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Validated
public class NewArticleDTO {
    private Long id;
    @Size(min = 1, max = 50)
    private String title;
    @Size(min = 1, max = 1000)
    private String content;
    private String createdDate = "0000-01-01T00:00:00";
    private Long userId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
