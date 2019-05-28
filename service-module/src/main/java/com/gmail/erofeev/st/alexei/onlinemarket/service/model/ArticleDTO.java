package com.gmail.erofeev.st.alexei.onlinemarket.service.model;

import java.util.ArrayList;
import java.util.List;

public class ArticleDTO {
    private static final int SHORT_CONTENT_LENGTH = 200;
    private Long id;
    private String title;
    private UserDTO user;
    private String content;
    private String createdDate;
    private boolean isDeleted = false;
    private boolean isHidden = false;
    private List<CommentDTO> comments = new ArrayList<>();
    private List<TagDTO> tags = new ArrayList<>();

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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
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

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public String getShortContent() {
        if (content.length() < SHORT_CONTENT_LENGTH) {
            return content;
        } else {
            return content.substring(0, SHORT_CONTENT_LENGTH) + "...";
        }
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "ArticleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", isDeleted=" + isDeleted +
                ", isHidden=" + isHidden +
                ", comments=" + comments +
                '}';
    }
}
