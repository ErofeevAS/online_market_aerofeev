package com.gmail.erofeev.st.alexei.onlinemarket.repository.model;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table
@SQLDelete(sql = "UPDATE comment SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user = new User();
    @Column
    private String content;
    @Column(name = "created_date")
    private Timestamp createdDate;
    @Column
    private boolean deleted = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article = new Article();

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }


    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return deleted == comment.deleted &&
                id.equals(comment.id) &&
                Objects.equals(content, comment.content) &&
                Objects.equals(createdDate, comment.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, createdDate, deleted);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", createdDate=" + createdDate +
                ", deleted=" + deleted +
                '}';
    }
}