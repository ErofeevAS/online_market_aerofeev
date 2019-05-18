package com.gmail.erofeev.st.alexei.onlinemarket.repository.impl;

import com.gmail.erofeev.st.alexei.onlinemarket.repository.CommentRepository;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.exception.RepositoryException;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.Comment;
import com.gmail.erofeev.st.alexei.onlinemarket.repository.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CommentRepositoryImpl extends GenericRepositoryImpl implements CommentRepository {
    private static final Logger logger = LoggerFactory.getLogger(CommentRepositoryImpl.class);

    @Override
    public List<Comment> getCommentsForArticleById(Connection connection, Long articleId) {
        List<Comment> comments = new ArrayList<>();
        String sql =
                "SELECT a.id" +
                        ", c.id as c_id,c.user_id AS comm_user_id,c.date AS c_date,c.content AS c_content,c.comment_parent_id AS c_parent_id" +
                        " FROM articles AS a" +
                        " JOIN users AS u ON a.user_id = u.id" +
                        " JOIN articles_comments AS a_c ON a.id = a_c.article_id" +
                        " JOIN comments AS c ON a_c.comment_id=c.id WHERE a.id =?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, articleId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    comments.add(getComment(resultSet));
                }
                return comments;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new RepositoryException(String.format("Can't get comments for articles with id: %s :", articleId + e.getMessage()), e);
        }
    }

    private Comment getComment(ResultSet resultSet) throws SQLException {
        Long commentId = resultSet.getLong("c_id");
        Long commentParentId = resultSet.getLong("c_parent_id");
        String commentContent = resultSet.getString("c_content");
        Timestamp commentDate = resultSet.getTimestamp("date");
        String commLastName = resultSet.getString("comm_lastname");
        String commFirstName = resultSet.getString("comm_firstname");
        long userId = resultSet.getLong("comm_user_id");
        User user = new User();
        user.setId(userId);
        user.setFirstName(commFirstName);
        user.setLastName(commLastName);

        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setContent(commentContent);
        comment.setDate(commentDate);
        comment.setUser(user);
        return comment;
    }
}
