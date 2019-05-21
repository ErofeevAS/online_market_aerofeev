package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.CommentRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl<Long, Comment> implements CommentRepository {
    private static final Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);

    @Override
    public void save(Connection connection, Long userId, Comment comment) {
        String content = comment.getContent();
        Timestamp date = comment.getDate();
        Long articleId = comment.getArticle().getId();
        String sql = "INSERT INTO comment (content,date,article_id,user_id) values(?,?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, content);
            preparedStatement.setTimestamp(2, date);
            preparedStatement.setLong(3, articleId);
            preparedStatement.setLong(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Database exception during saving a comment  for user with id %s: ", userId) + e.getMessage(), e);
        }
    }
}