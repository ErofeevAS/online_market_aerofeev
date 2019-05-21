package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;

import java.sql.Connection;
import java.util.List;

public interface CommentRepository extends GenericRepository<Long, Comment> {
    void save(Connection connection, Long userId, Comment comment);
}