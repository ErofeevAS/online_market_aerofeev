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
@SQLDelete(sql = "UPDATE review SET deleted = '1' WHERE id = ?")
@Where(clause = "deleted = '0'")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user = new User();
    @Column
    private String content;
    @Column
    private Timestamp date;
    @Column
    private Boolean deleted;
    @Column
    private Boolean hided;

    public Review() {
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Review review = (Review) o;
        return id.equals(review.id) &&
                Objects.equals(content, review.content) &&
                Objects.equals(date, review.date) &&
                Objects.equals(deleted, review.deleted) &&
                Objects.equals(hided, review.hided);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, content, date, deleted, hided);
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", user=" + user +
                ", content='" + content + '\'' +
                ", date=" + date +
                ", deleted=" + deleted +
                ", hided=" + hided +
                '}';
    }
}