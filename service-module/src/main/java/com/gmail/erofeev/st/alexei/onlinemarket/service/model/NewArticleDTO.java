package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
public class NewArticleDTO {
    private Long id;
    @Pattern(regexp = "^[A-Za-z0-9_ ]{1,40}$", message = "must be from 1 to 40, only English letters")
    private String title;
    @Pattern(regexp = "^[A-Za-z0-9_ ]{1,1000}$", message = "must be from 1 to 1000, only English letters")
    private String content;
    @NotEmpty(message = "must be not empty")
    @NotNull(message = "must be not null")
    private String date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
