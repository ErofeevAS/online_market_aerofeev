package com.gmail.erofeev.st.alexei.onlinemarket.repository;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;

import java.sql.Connection;
import java.util.List;

public interface CommentRepository extends GenericRepository {
    List<Comment> getCommentsForArticleById(Connection connection, Long articleId);
}
